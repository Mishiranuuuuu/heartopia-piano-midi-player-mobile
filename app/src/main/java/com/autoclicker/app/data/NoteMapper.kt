package com.autoclicker.app.data

/**
 * Maps MIDI note numbers to marker indices for each piano layout.
 *
 * This mirrors the logic in the Python reference scripts (piano/ folder):
 * - 15-key & 22-key: sharp notes snap to the nearest white key
 * - 37-key: all chromatic notes have a direct mapping
 * - Notes outside the layout's range are transposed by octaves to fit
 */
object NoteMapper {

    /**
     * Snap table: semitone index within an octave → nearest white-key semitone.
     * Used for 15-key and 22-key layouts which only have white keys.
     *
     *  0 C  → C   |  1 C# → C   |  2 D  → D   |  3 D# → D
     *  4 E  → E   |  5 F  → F   |  6 F# → F   |  7 G  → G
     *  8 G# → G   |  9 A  → A   | 10 A# → A   | 11 B  → B
     */
    private val SNAP_TO_WHITE = intArrayOf(
        0,  // C  → C
        0,  // C# → C
        2,  // D  → D
        2,  // D# → D
        4,  // E  → E
        5,  // F  → F
        5,  // F# → F
        7,  // G  → G
        7,  // G# → G
        9,  // A  → A
        9,  // A# → A
        11  // B  → B
    )

    // ─── 15-key layout (LAYOUT_5COL) ─────────────────────────────────────
    // 3 rows × 5 columns, white keys only, C4–C6 (MIDI 60–84)
    //
    // Marker indices (row-major order):
    //  0: C4(60)   1: D4(62)   2: E4(64)   3: F4(65)   4: G4(67)
    //  5: A4(69)   6: B4(71)   7: C5(72)   8: D5(74)   9: E5(76)
    // 10: F5(77)  11: G5(79)  12: A5(81)  13: B5(83)  14: C6(84)
    private val MAP_15 = mapOf(
        60 to 0,  62 to 1,  64 to 2,  65 to 3,  67 to 4,
        69 to 5,  71 to 6,  72 to 7,  74 to 8,  76 to 9,
        77 to 10, 79 to 11, 81 to 12, 83 to 13, 84 to 14
    )

    // ─── 22-key layout (LAYOUT_7COL) ─────────────────────────────────────
    // Row 0 (8 keys): higher octave, Row 1 (7): middle, Row 2 (7): lower
    // White keys only, C3–C6 (MIDI 48–84)
    //
    // Marker indices (row-major order from generateWhiteKeys()):
    //  0: C5(72)   1: D5(74)   2: E5(76)   3: F5(77)
    //  4: G5(79)   5: A5(81)   6: B5(83)   7: C6(84)
    //  8: C4(60)   9: D4(62)  10: E4(64)  11: F4(65)
    // 12: G4(67)  13: A4(69)  14: B4(71)
    // 15: C3(48)  16: D3(50)  17: E3(52)  18: F3(53)
    // 19: G3(55)  20: A3(57)  21: B3(59)
    private val MAP_22 = mapOf(
        72 to 0,  74 to 1,  76 to 2,  77 to 3,
        79 to 4,  81 to 5,  83 to 6,  84 to 7,
        60 to 8,  62 to 9,  64 to 10, 65 to 11,
        67 to 12, 69 to 13, 71 to 14,
        48 to 15, 50 to 16, 52 to 17, 53 to 18,
        55 to 19, 57 to 20, 59 to 21
    )

    // ─── 37-key layout (LAYOUT_7COL_SHARPS) ──────────────────────────────
    // 22 white keys + 15 black keys, C3–C6 (MIDI 48–84)
    // generateMarkers() returns: white keys first (22), then sharps (15)
    //
    // White keys (same order as MAP_22):   indices 0–21
    // Row 0 sharps (C#5,D#5,F#5,G#5,A#5): indices 22–26
    // Row 1 sharps (C#4,D#4,F#4,G#4,A#4): indices 27–31
    // Row 2 sharps (C#3,D#3,F#3,G#3,A#3): indices 32–36
    private val MAP_37 = buildMap {
        // White keys (same positions as 22-key)
        putAll(MAP_22)

        // Row 0 sharps: higher octave
        put(73, 22)  // C#5
        put(75, 23)  // D#5
        put(78, 24)  // F#5
        put(80, 25)  // G#5
        put(82, 26)  // A#5

        // Row 1 sharps: middle octave
        put(61, 27)  // C#4
        put(63, 28)  // D#4
        put(66, 29)  // F#4
        put(68, 30)  // G#4
        put(70, 31)  // A#4

        // Row 2 sharps: lower octave
        put(49, 32)  // C#3
        put(51, 33)  // D#3
        put(54, 34)  // F#3
        put(56, 35)  // G#3
        put(58, 36)  // A#3
    }

    /**
     * Map a MIDI note number to a marker index for the given layout.
     *
     * Steps:
     * 1. Clamp the note into the layout's octave range (transpose by ±12)
     * 2. For white-key-only layouts, snap sharps to the nearest white key
     * 3. Look up the marker index in the layout's mapping table
     *
     * @return marker index, or -1 if unmappable
     */
    fun getMappedMarkerIndex(midiNote: Int, layoutType: LayoutType): Int {
        return when (layoutType) {
            LayoutType.LAYOUT_5COL -> mapNote15(midiNote)
            LayoutType.LAYOUT_7COL -> mapNote22(midiNote)
            LayoutType.LAYOUT_7COL_SHARPS -> mapNote37(midiNote)
        }
    }

    private fun mapNote15(note: Int): Int {
        // Clamp to C4–C6 range (60–84)
        var n = note
        while (n < 60) n += 12
        while (n > 84) n -= 12

        // Snap to white key
        val octaveBase = (n / 12) * 12
        val noteInOctave = n % 12
        val snapped = octaveBase + SNAP_TO_WHITE[noteInOctave]

        return MAP_15[snapped] ?: -1
    }

    private fun mapNote22(note: Int): Int {
        // Clamp to C3–C6 range (48–84)
        var n = note
        while (n < 48) n += 12
        while (n > 84) n -= 12

        // Snap to white key
        val octaveBase = (n / 12) * 12
        val noteInOctave = n % 12
        val snapped = octaveBase + SNAP_TO_WHITE[noteInOctave]

        return MAP_22[snapped] ?: -1
    }

    private fun mapNote37(note: Int): Int {
        // Clamp to C3–C6 range (48–84)
        var n = note
        while (n < 48) n += 12
        while (n > 84) n -= 12

        // Direct mapping — no snapping needed, all chromatic notes are available
        return MAP_37[n] ?: -1
    }
}
