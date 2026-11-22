# BCD (Binary Coded Decimal) Arithmetic — Superseded

**Status:** Superseded. This file is kept as a pointer only.

The BCD requirements (version 1.0, 2026-05-14) have been merged into the main documentation on branch `feature/bcd-arithmetic`:

- **Design, decision register, class design, function support, test plan:** `doc/GD-BASIC_Detailed_Design.md`, section "BCD Arithmetic Design (planned, branch `feature/bcd-arithmetic`)".
- **Language rules (`#` suffix, precision, overflow):** `doc/BASIC.md`, section "Variables, Arrays, and Constants".
- **User-facing description:** `doc/USER_GUIDE.md`, section "Variable Types and Suffixes".

The main changes from version 1.0 are:

- BCD is chosen per variable with the `#` suffix. The global mode (`--bcd`, `-B`) and the `#PRAGMA BCD_*` directives are removed.
- The precision is 40 significant digits, not 10 decimal places.
- All maths functions run in BCD, not by converting to `double`.
- Overflow raises an error beyond 40 digits in the whole-number part.
- `#` is the BCD suffix. Untyped variables stay REAL, and `!` is BOOLEAN (planned; see the decision register).

The full list of decisions and open questions is in the design document, section "Decision register".
