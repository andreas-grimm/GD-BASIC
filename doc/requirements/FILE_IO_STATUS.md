# GD-BASIC File I/O Status Report

**Interpreter Version:** 0.1.0  
**Date:** 2026-04-30

---

## File I/O Operations Implemented

### 1. FOPEN - Open Files
**Status:** ✅ Implemented

**Syntax:**
```basic
FOPEN FileID "FileName" "Mode"
```

**Parameters:**
- `FileID` - Numeric identifier (1-255) for the file reference
- `FileName` - String path to the file
- `Mode` - Either `"READ"` or `"WRITE"`

**Example:**
```basic
10 FOPEN 1 "data.txt" "READ"
20 REM File 1 is now open for reading
```

**Implementation Details:**
- Uses `FileManager.openFile()` method
- Supports READ mode for input operations
- Supports WRITE mode for output operations
- File is stored in memory with the FileID as key

---

### 2. FPRINT - Write to Files
**Status:** ✅ Implemented

**Syntax:**
```basic
FPRINT FileID Expression [, Expression]
FPRINT FileID Expression [, Expression];
```

**Parameters:**
- `FileID` - File identifier from FOPEN
- `Expression` - Value to write to file
- `,` - Column separator
- `;` - Suppress line break (no newline)

**Example:**
```basic
10 FOPEN 1 "output.txt" "WRITE"
20 FPRINT 1 "Name: ", "John", " Age: ", 30
30 FPRINT 1 "Data ";    REM No newline
40 FPRINT 1 "continues"
50 FCLOSE 1
```

**Implementation Details:**
- Evaluates expressions to strings
- Writes to file via `FileManager.write()`
- Supports formatting with commas and semicolons
- Uses BufferedWriter for efficiency

---

### 3. FINPUT - Read from Files
**Status:** ✅ Implemented

**Syntax:**
```basic
FINPUT FileID Variable
```

**Parameters:**
- `FileID` - File identifier from FOPEN
- `Variable` - Variable to store the read value

**Example:**
```basic
10 FOPEN 1 "input.txt" "READ"
20 FINPUT 1 LINE$
30 PRINT "Read: ", LINE$
40 FCLOSE 1
```

**Implementation Details:**
- Reads next line from file via `FileManager.read()`
- Stores value in the specified variable
- Variable type is determined by suffix ($ for string, # for real, etc.)
- Handles line-by-line input

---

### 4. FCLOSE - Close Files
**Status:** ✅ Implemented

**Syntax:**
```basic
FCLOSE FileID
FCLOSE FileID "DELETE"
```

**Parameters:**
- `FileID` - File identifier from FOPEN
- `"DELETE"` - Optional parameter to delete file on close

**Example:**
```basic
10 FOPEN 1 "temp.txt" "WRITE"
20 FPRINT 1 "Temporary data"
30 FCLOSE 1 "DELETE"     REM Delete file when closing
```

Or:
```basic
10 FOPEN 2 "keep.txt" "WRITE"
20 FPRINT 2 "Important data"
30 FCLOSE 2              REM Keep file when closing
```

**Implementation Details:**
- Closes file via `FileManager.closeFile()`
- Optional deletion on close
- Flushes BufferedWriter before closing
- Removes file from FileManager's file table

---

### 5. EOF() - Check End of File
**Status:** ✅ Implemented

**Syntax:**
```basic
IF EOF(FileID) THEN ...
```

**Parameters:**
- `FileID` - File identifier from FOPEN

**Example:**
```basic
10 FOPEN 1 "data.txt" "READ"
20 WHILE NOT EOF(1)
30   FINPUT 1 LINE$
40   PRINT LINE$
50 END-WHILE
60 FCLOSE 1
```

**Implementation Details:**
- Returns 1 if end-of-file reached, 0 otherwise
- Uses `FileManager.getEOF()` method
- Essential for detecting end of input

---

## File I/O Operations Missing/Not Implemented

### 1. Append Mode ❌
**Status:** Not Implemented

**Missing Syntax:**
```basic
FOPEN FileID "FileName" "APPEND"
```

**Why:** Current implementation only supports READ and WRITE modes.

**Workaround:** Open file in READ mode, read all content, close, then open in WRITE mode and write original content plus new content.

---

### 2. Seek Operations (Random Access) ❌
**Status:** Not Implemented

**Missing Functionality:**
- Cannot seek to specific byte/line positions
- No SEEK or POSITION statements
- Only sequential read/write supported

**Example (Not Supported):**
```basic
10 FOPEN 1 "data.txt" "READ"
20 SEEK 1 100              REM Skip to byte 100 (NOT AVAILABLE)
30 FINPUT 1 DATA$
```

---

### 3. File Status Queries ❌
**Status:** Partially Implemented (internal only)

**Missing Statements:**
- No LOF() function (Length of File)
- No FILE_EXISTS() or similar
- No file size query
- No file attribute access

**Not Supported:**
```basic
10 IF LOF(1) > 1000 THEN ...    REM File size (NOT AVAILABLE)
20 IF FILE_EXISTS("file.txt") THEN ...  REM Check existence (NOT AVAILABLE)
```

**Internal Support:** `FileManager` has `getFileStatus()` but not exposed to BASIC programs.

---

### 4. Directory Operations ❌
**Status:** Not Implemented

**Missing Operations:**
- Cannot list directory contents
- No file deletion (except via FCLOSE "DELETE")
- No directory creation/deletion
- No path manipulation

**Not Supported:**
```basic
10 REM Directory operations not supported
20 DIR$ = DIR("*.txt")        REM NOT AVAILABLE
30 DELETE "oldfile.txt"        REM NOT AVAILABLE
40 MKDIR "newdir"              REM NOT AVAILABLE
```

---

### 5. Binary File Operations ❌
**Status:** Not Implemented

**Missing Operations:**
- No binary read/write
- No byte-level access
- No record operations
- All files treated as text

**Not Supported:**
```basic
10 REM Binary operations not supported
20 FREAD FileID, Buffer, Length    REM NOT AVAILABLE
30 FWRITE FileID, Buffer, Length   REM NOT AVAILABLE
```

---

### 6. Multiple Lines Read/Write ❌
**Status:** Limited (Line-by-line only)

**Current Limitation:**
- FINPUT reads one line at a time
- No bulk read operation
- No array input from file

**Not Supported:**
```basic
10 FOPEN 1 "data.txt" "READ"
20 FINPUT 1 ARRAY%()           REM Read entire array (NOT AVAILABLE)
30 FCLOSE 1
```

---

### 7. File Positioning Queries ❌
**Status:** Not Implemented

**Missing Functions:**
- No LOC() function (current position)
- No line number tracking
- No byte position access

**Not Supported:**
```basic
10 FOPEN 1 "data.txt" "READ"
20 FINPUT 1 LINE$
30 POS% = LOC(1)              REM Get current position (NOT AVAILABLE)
```

---

## Recommended File I/O Patterns

### Reading an Entire File
```basic
10 FOPEN 1 "data.txt" "READ"
20 WHILE NOT EOF(1)
30   FINPUT 1 LINE$
40   PRINT LINE$
50   GOSUB 100           REM Process line
60 END-WHILE
70 FCLOSE 1
80 END

100 REM Process line subroutine
110 REM Add your processing here
120 RETURN
```

### Writing to a File
```basic
10 FOPEN 1 "output.txt" "WRITE"
20 FOR I% = 1 TO 100
30   FPRINT 1 "Line "; I%; ": Data"
40 NEXT
50 FCLOSE 1
60 END
```

### Temporary File Processing
```basic
10 FOPEN 1 "temp.txt" "WRITE"
20 FPRINT 1 "Temporary data"
30 FPRINT 1 "More data"
40 FCLOSE 1 "DELETE"    REM Delete when done
50 END
```

---

## Summary Table

| Feature | Status | Notes |
|---------|--------|-------|
| FOPEN (READ) | ✅ | Fully supported |
| FOPEN (WRITE) | ✅ | Fully supported |
| FOPEN (APPEND) | ❌ | Not implemented |
| FPRINT | ✅ | Line-based output |
| FINPUT | ✅ | Line-by-line input |
| FCLOSE | ✅ | With optional delete |
| EOF() | ✅ | End-of-file detection |
| File deletion | ✅ | Via FCLOSE "DELETE" |
| Seek/Random Access | ❌ | Not available |
| Binary operations | ❌ | Not available |
| Directory operations | ❌ | Not available |
| File queries (LOF, LOC) | ❌ | Not available |
| Bulk operations | ❌ | Line-by-line only |

---

## Technical Implementation Details

### FileManager Architecture
- **Location:** `src/main/java/eu/gricom/basic/memoryManager/FileManager.java`
- **Approach:** In-memory file management using BufferedReader/BufferedWriter
- **File Table:** HashMap keyed by FileID
- **Types:** `FileOpenType` enum (READ, WRITE)

### File Handle Management
- Each open file is assigned a unique FileID (1-255)
- Multiple files can be open simultaneously
- File handles are stored in FileManager singleton

### Limitations
- All I/O is text-based (line-oriented)
- No binary file support
- Sequential access only
- Lines are assumed to be newline-terminated

---

## Recommendations for Future Enhancement

1. **Priority 1 (High Value):**
   - Append mode (FOPEN with "APPEND")
   - File existence checking function
   - Bulk file size query (LOF function)

2. **Priority 2 (Medium Value):**
   - Random access with SEEK function
   - Multiple file simultaneous operations improvement
   - Better error reporting for file operations

3. **Priority 3 (Lower Value):**
   - Binary file support
   - Directory operations
   - File attribute access
   - Pattern matching for files

---

## Conclusion

The GD-BASIC interpreter provides **adequate basic file I/O** for simple text file operations:
- ✅ **Strengths:** Simple sequential read/write, line-based processing, file deletion
- ❌ **Limitations:** No append mode, no random access, no binary support, no directory operations

For programs requiring advanced file operations (append, seek, binary), these features would need to be added to the FileManager and parser.