
# File Operations Enhancement Requirements

**Version**: 1.0  
**Date**: 2026-05-14  
**Status**: Requirements Specification

---

## Executive Summary

This document specifies enhancements to the file I/O system in GD-BASIC. The current implementation supports basic file operations (FOPEN, FCLOSE, FPRINT, FINPUT, EOF). This specification identifies gaps and proposes a comprehensive, robust file operations suite covering sequential I/O, file system operations, and data manipulation.

---

## 1. Current State Analysis

### 1.1 Existing File Operations

**Location**: `eu.gricom.basic.memoryManager.FileManager`

| Operation | Function | Implemented | Status |
|-----------|----------|-------------|--------|
| Open file | `FOPEN(filename, mode, id)` | ✅ Yes | Basic |
| Close file | `FCLOSE(id)` | ✅ Yes | Basic |
| Read line | `FINPUT(id)` | ✅ Yes | Works |
| Write line | `FPRINT(id, data)` | ✅ Yes | Works |
| Check EOF | `EOF(id)` | ✅ Yes | Basic |
| Get filename | `GETFILENAME(id)` | ✅ Yes | Internal only |

### 1.2 Identified Gaps

**Critical Missing Features**:
- ❌ File existence checking
- ❌ Directory operations (create, list, delete)
- ❌ File deletion/renaming
- ❌ Seek/positioning operations
- ❌ File size/attributes queries
- ❌ Binary file operations
- ❌ Line counting / record operations
- ❌ File locking mechanisms
- ❌ Append mode support (only read/write)
- ❌ Character-level I/O (only line-based)

**Functional Limitations**:
- Single file handle per operation (no multiple concurrent file access)
- No file position tracking
- No binary data support
- No random access
- No file permissions/attributes

---

## 2. Proposed File Operations

### 2.1 Core File Operations (Tier 1 - Essential)

#### 2.1.1 File Access Operations

| Operation | Syntax | Purpose | Returns |
|-----------|--------|---------|---------|
| **File Open** | `id = FOPEN(filename$, mode$)` | Open file for I/O | File ID (int) or -1 |
| **File Close** | `FCLOSE(id)` | Close file, save changes | Success (0) or error code |
| **File Exists** | `exists = FILEEXISTS(filename$)` | Check if file exists | Boolean (1/0) |
| **Delete File** | `result = FILEDEL(filename$)` | Delete file from disk | Success code |
| **Rename File** | `result = FILERENAME(oldname$, newname$)` | Rename/move file | Success code |
| **Get File Size** | `size = FILESIZE(filename$)` | Get file size in bytes | File size (long) |
| **Get Mod Time** | `timestamp = FILETIME(filename$)` | Get last modification time | Unix timestamp |

**Open Modes**:
- `"r"` - Read mode (file must exist)
- `"w"` - Write mode (create/truncate)
- `"a"` - Append mode (create/add to end)
- `"rb"` - Read binary
- `"wb"` - Write binary
- `"ab"` - Append binary

#### 2.1.2 File Reading Operations

| Operation | Syntax | Purpose | Returns |
|-----------|--------|---------|---------|
| **Read Line** | `line$ = FINPUT(id)` | Read entire line | String or EOF marker |
| **Read Char** | `char$ = FGETC(id)` | Read single character | Character string |
| **Read Bytes** | `data$ = FREAD(id, count)` | Read N bytes | Binary string |
| **Peek Char** | `char$ = FPEEK(id)` | Look at next char (no advance) | Character string |
| **Read Line Count** | `count = FLINECT(id)` | Get current line number | Line count |

#### 2.1.3 File Writing Operations

| Operation | Syntax | Purpose | Returns |
|-----------|--------|---------|---------|
| **Write Line** | `FPRINT(id, data$)` | Write string + newline | Success code |
| **Write Char** | `FPUTC(id, char$)` | Write single character | Success code |
| **Write Bytes** | `FWRITE(id, data$)` | Write raw bytes | Bytes written |
| **Write Formatted** | `FPRINTF(id, format$, ...)` | Printf-style formatting | Success code |

#### 2.1.4 File Positioning Operations

| Operation | Syntax | Purpose | Returns |
|-----------|--------|---------|---------|
| **Seek Absolute** | `FSEEK(id, position)` | Go to byte position | New position or -1 |
| **Seek Relative** | `FSEEK(id, offset, whence)` | Seek from current/end | New position or -1 |
| **Get Position** | `pos = FTELL(id)` | Get current byte position | Byte position |
| **Go to Line** | `result = FGOLINE(id, linenum)` | Jump to line number | Success code |
| **Rewind** | `FREWIND(id)` | Go to file start | Success code |

#### 2.1.5 File Query Operations

| Operation | Syntax | Purpose | Returns |
|-----------|--------|---------|---------|
| **EOF Check** | `status = EOF(id)` | Check end-of-file | Boolean (1/0) |
| **Is Open** | `status = FISOPEN(id)` | Check if file ID is open | Boolean (1/0) |
| **Get Mode** | `mode$ = FGETMODE(id)` | Get file open mode | Mode string |
| **Get Filename** | `filename$ = FGETNAME(id)` | Get filename | Filename string |
| **File Empty** | `status = FILEEMPTY(filename$)` | Check if file is empty | Boolean (1/0) |

### 2.2 Directory Operations (Tier 2 - Enhanced)

| Operation | Syntax | Purpose | Returns |
|-----------|--------|---------|---------|
| **Directory Exists** | `exists = DIREXISTS(path$)` | Check directory exists | Boolean (1/0) |
| **Create Directory** | `result = MKDIR(path$)` | Create directory | Success code |
| **Delete Directory** | `result = RMDIR(path$)` | Delete empty directory | Success code |
| **List Files** | `count = DIRLIST(path$, pattern$, array$())` | List directory contents | File count |
| **Get Current Dir** | `path$ = GETCWD()` | Get working directory | Path string |
| **Change Directory** | `result = CHDIR(path$)` | Change working directory | Success code |

### 2.3 File System Operations (Tier 3 - Advanced)

| Operation | Syntax | Purpose | Returns |
|-----------|--------|---------|---------|
| **Copy File** | `result = FILECOPY(src$, dst$)` | Copy file | Success code |
| **Compare Files** | `result = FILECMP(file1$, file2$)` | Binary file comparison | 0 if equal, -1/1 if diff |
| **Get File Type** | `type$ = FILETYPE(filename$)` | Get file classification | Type string |
| **File Locked** | `locked = FILELOCKED(filename$)` | Check if file in use | Boolean (1/0) |
| **Sync to Disk** | `FFLUSH(id)` | Force write to disk | Success code |

---

## 3. Detailed Implementation Plan

### 3.1 New Classes to Create

#### 3.1.1 FileOperations Class

**File**: `src/main/java/eu/gricom/basic/memoryManager/FileOperations.java`

```java
public final class FileOperations {
    // File access
    public static IntegerValue fileExists(String strFileName)
    public static IntegerValue fileDelete(String strFileName)
    public static IntegerValue fileRename(String strOldName, String strNewName)
    public static LongValue fileSize(String strFileName)
    public static LongValue fileTime(String strFileName)
    
    // File content operations
    public static StringValue readChar(int iFileId)
    public static StringValue peekChar(int iFileId)
    public static StringValue readBytes(int iFileId, int iCount)
    public static IntegerValue writeChar(int iFileId, String strChar)
    public static IntegerValue writeBytes(int iFileId, String strData)
    public static IntegerValue fileLineCount(int iFileId)
    
    // File positioning
    public static LongValue fileSeek(int iFileId, long lPosition)
    public static LongValue fileSeek(int iFileId, long lOffset, int iWhence)
    public static LongValue fileTell(int iFileId)
    public static IntegerValue fileGoLine(int iFileId, int iLineNum)
    public static IntegerValue fileRewind(int iFileId)
    
    // File queries
    public static IntegerValue isFileOpen(int iFileId)
    public static StringValue getFileMode(int iFileId)
    public static IntegerValue fileEmpty(String strFileName)
    
    // Directory operations
    public static IntegerValue dirExists(String strPath)
    public static IntegerValue mkdir(String strPath)
    public static IntegerValue rmdir(String strPath)
    public static StringValue getCwd()
    public static IntegerValue chdir(String strPath)
    
    // Advanced operations
    public static IntegerValue fileCopy(String strSrc, String strDst)
    public static IntegerValue fileCmp(String strFile1, String strFile2)
    public static StringValue fileType(String strFileName)
    public static IntegerValue fileLocked(String strFileName)
    public static IntegerValue fileFlush(int iFileId)
}
```

#### 3.1.2 DirectoryIterator Class

**File**: `src/main/java/eu/gricom/basic/memoryManager/DirectoryIterator.java`

Supports `DIRLIST()` operation:
```java
public class DirectoryIterator {
    public List<String> listFiles(String strPath, String strPattern)
    public List<String> listDirectories(String strPath)
    public List<String> listAll(String strPath)
}
```

#### 3.1.3 FileSeekMode Enum

**File**: `src/main/java/eu/gricom/basic/memoryManager/FileSeekMode.java`

```java
public enum FileSeekMode {
    SEEK_SET(0),    // Absolute position
    SEEK_CUR(1),    // Relative to current
    SEEK_END(2);    // Relative to end
}
```

### 3.2 Modifications to Existing Classes

#### 3.2.1 FileManager Class

**File**: `src/main/java/eu/gricom/basic/memoryManager/FileManager.java`

**Additions**:
```java
// New methods
public boolean fileExists(String strFileName)
public boolean isFileOpen(int iFileId)
public int deleteFile(String strFileName)
public int renameFile(String strOldName, String strNewName)
public long getFileSize(String strFileName)
public long getFileModTime(String strFileName)
public String getFileMode(int iFileId)

// Positioning
public long seekFile(int iFileId, long lPosition)
public long seekFile(int iFileId, long lOffset, int iWhence)
public long tellFile(int iFileId)
public int rewindFile(int iFileId)

// Character I/O
public char readChar(int iFileId)
public char peekChar(int iFileId)
public int writeChar(int iFileId, char cChar)
public String readBytes(int iFileId, int iCount)
public int writeBytes(int iFileId, String strData)

// Utility
public int flushFile(int iFileId)
public int getLineNumber(int iFileId)
```

**Implementation Notes**:
- Extend internal file tracking to include position information
- Add binary mode support alongside text mode
- Implement proper RandomAccessFile for seeking
- Track line numbers during reads

#### 3.2.2 Execute Class

**File**: `src/main/java/eu/gricom/basic/runtimeManager/Execute.java`

**Additions**:
- Register FileOperations functions as built-in BASIC functions
- Examples: `FILEEXISTS(name$)`, `MKDIR(path$)`, `FSEEK(id, pos)`, etc.

### 3.3 New Functions to Create

#### 3.3.1 File Query Functions

**File**: `src/main/java/eu/gricom/basic/functions/Fileexists.java`
```java
public class Fileexists implements Function {
    // Check if file exists
    public Value evaluate(List<Value> aoParams, VariableManagement oVariableManagement)
}
```

**Similar classes**:
- `Filedel.java` - Delete file
- `Filerename.java` - Rename file
- `Filesize.java` - Get file size
- `Filetime.java` - Get modification time
- `Fileempty.java` - Check if file is empty
- `Filetype.java` - Determine file type
- `Filelocked.java` - Check if file locked
- `Filecmp.java` - Compare files

#### 3.3.2 File I/O Functions

**File**: `src/main/java/eu/gricom/basic/functions/Fgetc.java`
```java
public class Fgetc implements Function {
    // Read single character
    public Value evaluate(List<Value> aoParams, VariableManagement oVariableManagement)
}
```

**Similar classes**:
- `Fputc.java` - Write character
- `Fpeek.java` - Peek character without advancing
- `Fread.java` - Read N bytes
- `Fwrite.java` - Write bytes
- `Fprintf.java` - Formatted write (printf-style)
- `Flinect.java` - Get line count
- `Fgetmode.java` - Get file mode
- `Fgetname.java` - Get filename
- `Fisopen.java` - Check if file open

#### 3.3.3 File Positioning Functions

**File**: `src/main/java/eu/gricom/basic/functions/Fseek.java`
```java
public class Fseek implements Function {
    // Seek in file
    public Value evaluate(List<Value> aoParams, VariableManagement oVariableManagement)
}
```

**Similar classes**:
- `Ftell.java` - Get file position
- `Fgoline.java` - Go to line number
- `Frewind.java` - Rewind to start
- `Fflush.java` - Flush to disk

#### 3.3.4 Directory Functions

**File**: `src/main/java/eu/gricom/basic/functions/Direxists.java`
```java
public class Direxists implements Function {
    // Check if directory exists
    public Value evaluate(List<Value> aoParams, VariableManagement oVariableManagement)
}
```

**Similar classes**:
- `Mkdir.java` - Create directory
- `Rmdir.java` - Remove directory
- `Dirlist.java` - List directory contents
- `Getcwd.java` - Get current working directory
- `Chdir.java` - Change directory

---

## 4. Code Changes Summary

### 4.1 Files to Create

| Category | File | Purpose | Est. Lines |
|----------|------|---------|-----------|
| Core | `FileOperations.java` | Static utility methods | 400-500 |
| Core | `DirectoryIterator.java` | Directory traversal | 100-150 |
| Core | `FileSeekMode.java` | Seek mode enumeration | 20-30 |
| Tests | `FileOperationsTest.java` | Unit tests | 200-300 |
| Functions | `Fileexists.java` | File exists function | 40-50 |
| Functions | `Filedel.java` | File delete function | 40-50 |
| Functions | `Fgetc.java` | Read char function | 40-50 |
| Functions | `Fputc.java` | Write char function | 40-50 |
| Functions | `Fseek.java` | File seek function | 50-70 |
| Functions | `Mkdir.java` | Make directory function | 40-50 |
| Functions | `Dirlist.java` | List directory function | 60-80 |
| **Subtotal** | **~12 new files** | | **1,150-1,450 lines** |

### 4.2 Files to Modify

| File | Changes | Impact |
|------|---------|--------|
| `FileManager.java` | Add 20+ new methods for positioning, character I/O, utilities | **Medium** (80-120 lines) |
| `Execute.java` | Register 25+ new file operation functions | **Low** (50-80 lines) |
| `Function.java` | No changes (interface unchanged) | **None** |
| `BasicParserTest.java` | Add integration tests for file operations | **Low** (50-100 lines) |

**Total Modifications**: ~180-300 lines of changes to existing code

---

## 5. Function Grouping by Priority

### Phase 1: Essential (Week 1)
- FILEEXISTS, FILEDEL, FILERENAME
- FILESIZE, FILETIME
- FGETC, FPUTC
- FSEEK, FTELL, FREWIND
- FISOPEN, FGETMODE, FGETNAME

### Phase 2: Enhanced (Week 2)
- FREAD, FWRITE
- FPEEK, FLINECT
- DIREXISTS, MKDIR, RMDIR
- GETCWD, CHDIR

### Phase 3: Advanced (Week 3)
- FPRINTF (formatted output)
- DIRLIST
- FILECOPY, FILECMP
- FILETYPE, FILELOCKED, FFLUSH
- FILEEMPTY

---

## 6. Error Handling

### 6.1 Error Codes

| Code | Meaning | Recovery |
|------|---------|----------|
| 0 | Success | Continue |
| -1 | File not found | Handle in BASIC code |
| -2 | Permission denied | Handle in BASIC code |
| -3 | Invalid file ID | Handle in BASIC code |
| -4 | File already open | Close first |
| -5 | File locked | Retry or skip |
| -6 | Out of disk space | Stop operation |
| -7 | Directory not empty | Remove files first |
| -8 | Path not found | Handle in BASIC code |

### 6.2 Exception Handling Strategy

All file operations should:
1. Catch `IOException` and map to error codes
2. Return negative error codes on failure
3. Log errors when appropriate
4. Never crash the interpreter
5. Allow BASIC code to handle errors

```java
public static IntegerValue fileDelete(String strFileName) {
    try {
        Files.delete(Paths.get(strFileName));
        return new IntegerValue(0);  // Success
    } catch (NoSuchFileException e) {
        return new IntegerValue(-1);  // Not found
    } catch (AccessDeniedException e) {
        return new IntegerValue(-2);  // Permission denied
    } catch (IOException e) {
        return new IntegerValue(-6);  // Other error
    }
}
```

---

## 7. Testing Strategy

### 7.1 Unit Tests (FileOperationsTest.java)

```java
@Test
void testFileExists() {
    IntegerValue result = FileOperations.fileExists("test.txt");
    // Test with existing and non-existing files
}

@Test
void testSeekAndTell() {
    // Open file, seek to position, verify FTELL
}

@Test
void testReadWriteChar() {
    // Write characters, read them back, verify
}

@Test
void testDirectoryOperations() {
    // Create directory, list, delete
}
```

### 7.2 System Tests (BASIC Programs)

**test_file_operations.bas**:
```basic
10 FILENAME$ = "test.txt"
20 IF FILEEXISTS(FILENAME$) THEN FILEDEL(FILENAME$)
30 ID = FOPEN(FILENAME$, "w")
40 FPRINT(ID, "Line 1")
50 FPRINT(ID, "Line 2")
60 FCLOSE(ID)
70 SIZE = FILESIZE(FILENAME$)
80 PRINT "File size: "; SIZE
90 END
```

**test_file_seeking.bas**:
```basic
10 ID = FOPEN("data.txt", "r")
20 FSEEK(ID, 10)
30 POS = FTELL(ID)
40 CHAR$ = FGETC(ID)
50 FCLOSE(ID)
60 END
```

**test_directory_operations.bas**:
```basic
10 IF NOT DIREXISTS("testdir") THEN MKDIR("testdir")
20 COUNT = DIRLIST("testdir", "*.*", FILES$())
30 PRINT "Found "; COUNT; " files"
40 RMDIR("testdir")
50 END
```

---

## 8. BASIC Language Integration

### 8.1 New Built-in Functions

These functions integrate seamlessly with BASIC:

```basic
! File existence check
IF FILEEXISTS("data.txt") THEN
    ID = FOPEN("data.txt", "r")
ELSE
    PRINT "File not found"
END IF

! Character-level I/O
ID = FOPEN("input.txt", "r")
WHILE NOT EOF(ID)
    CHAR$ = FGETC(ID)
    PRINT CHAR$;
WEND
FCLOSE(ID)

! File positioning
ID = FOPEN("data.txt", "r")
FSEEK(ID, 100)            ! Go to byte 100
DATA$ = FREAD(ID, 50)     ! Read 50 bytes
FCLOSE(ID)

! Directory operations
IF NOT DIREXISTS("backup") THEN MKDIR("backup")
FILECOPY("data.txt", "backup/data.txt")
```

---

## 9. Backward Compatibility

### 9.1 Guarantees

- Existing file operations (FOPEN, FCLOSE, FINPUT, FPRINT, EOF) work unchanged
- No breaking changes to existing API
- New functions use different names (FGETC vs FINPUT)
- All new functions are optional enhancements

### 9.2 Migration Path

Applications using basic file I/O can incrementally adopt new functions:
1. Continue using FOPEN/FCLOSE/FPRINT/FINPUT
2. Add FILEEXISTS checks for robustness
3. Migrate to FGETC/FPUTC for character-level I/O
4. Use FSEEK for random access when needed

---

## 10. Performance Considerations

### 10.1 Buffering

- Text mode: Use BufferedReader/BufferedWriter
- Binary mode: Use RandomAccessFile for seeking
- Line counting: Cache line offsets for FGOLINE efficiency

### 10.2 Resource Management

- Limit concurrent open files (e.g., max 256)
- Auto-close files on program exit
- Warn if > 100 files opened in one session
- Track file handles to prevent leaks

### 10.3 Optimization

- Cache directory listings (with TTL)
- Lazy-load file attributes
- Use NIO for better performance on large files

---

## 11. New File Operation Statements (Implementation - May 30, 2026)

Four new file operation statements have been implemented and integrated into BasicParser:

### 11.1 FPEEK Statement - Character Lookahead

**Syntax**: `FPEEK fileId, variableName`

**Purpose**: Read next character from file without advancing read position

**Implementation Details**:
- Retrieves current read position from FileManager
- Closes and reopens file to safely access position
- Reads lines sequentially, accumulating character count
- Extracts character at current position
- Preserves position for subsequent FGET or FPEEK operations
- Returns "EOF" if end of file is reached

**Parser Integration** (BasicParser.java lines 348-367):
```java
case FPEEK: {
    String strVariableName;
    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FPEEK] ");
    iOrgPosition = _iPosition;
    _iPosition++;
    
    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
    consumeToken(BasicTokenType.COMMA);
    strVariableName = consumeToken(BasicTokenType.WORD).getText();
    
    aoStatements.add(new FPeekStatement(iOrgPosition, iFileId, strVariableName));
}
break;
```

**Unit Test Coverage**: 9 tests in FPeekStatementTest.java
- Peek first character
- Multiple consecutive peeks return same character
- Peek with multiline files, Unicode, special characters
- Empty file handling
- Large file handling

**Use Case Example**:
```basic
10 FOPEN 1 "data.txt" "r"
20 FPEEK 1, C$       ! Look at next char without consuming
30 IF C$ = "X" THEN GOSUB 1000
40 FGET 1, C$        ! Now read it
50 END
```

### 11.2 FPUT Statement - Character Output Without Newline

**Syntax**: `FPUT fileId, expression`

**Purpose**: Write character/string to file without adding newline terminator

**Implementation Details**:
- Evaluates expression to obtain string value
- Creates single-element list containing expression
- Delegates to FPrintStatement with bCRLF=false flag
- Enables building lines character-by-character
- Useful for formatted output composition

**Parser Integration** (BasicParser.java lines 369-388):
```java
case FPUT: {
    Expression oExpression;
    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FPUT] ");
    iOrgPosition = _iPosition;
    _iPosition++;
    
    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
    consumeToken(BasicTokenType.COMMA);
    oExpression = expression();
    
    aoStatements.add(new FPutStatement(iOrgPosition, iFileId, oExpression));
}
break;
```

**Unit Test Coverage**: 10 tests in FPutStatementTest.java
- Single characters, multi-character strings
- Empty strings, special characters (\n, \t)
- Long strings, Unicode characters
- Numeric strings, path strings

**Use Case Example**:
```basic
10 FOPEN 1 "output.txt" "w"
20 FPUT 1, "H"
30 FPUT 1, "e"
40 FPUT 1, "l"
50 FPUT 1, "l"
60 FPUT 1, "o"
70 FPRINT 1 ""           ! Add newline
80 FCLOSE 1
90 END
```

### 11.3 FRENAME Statement - File Renaming/Moving

**Syntax**: `FRENAME fileId, newFileName`

**Purpose**: Rename or move file tracked by file ID

**Implementation Details**:
- Verifies file ID is registered in FileManager
- Retrieves current filename from FileManager
- Closes file without deleting (preserves content)
- Renames/moves file using Files.move() API
- Re-registers file with same ID under new filename
- Enables moving files to different directories

**Parser Integration** (BasicParser.java lines 390-411):
```java
case FRENAME: {
    StringValue oNewFileName;
    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FRENAME] ");
    iOrgPosition = _iPosition;
    _iPosition++;
    
    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
    consumeToken(BasicTokenType.COMMA);
    oNewFileName = new StringValue(consumeToken(BasicTokenType.STRING).getText());
    
    aoStatements.add(new FRenameStatement(iOrgPosition, iFileId, oNewFileName));
}
break;
```

**Error Handling**:
- Throws RuntimeException if file ID not registered
- Throws RuntimeException if file cannot be closed
- Throws RuntimeException if rename operation fails (permissions, target exists)

**Unit Test Coverage**: 10 tests in FRenameStatementTest.java
- Simple filenames, paths, different extensions
- Names without extensions, case sensitivity
- Special characters (hyphens, underscores)
- Hidden files, long filenames

**Use Case Example**:
```basic
10 FOPEN 1 "temp.txt" "w"
20 FPRINT 1 "Data to process"
30 FCLOSE 1
40 FRENAME 1, "final.txt"   ! Rename file
50 IF FEXISTS("final.txt") THEN PRINT "Success"
60 END
```

### 11.4 FREWIND Statement - File Position Reset

**Syntax**: `FREWIND fileId`

**Purpose**: Reset file read position to beginning without closing file

**Implementation Details**:
- Verifies file ID is registered in FileManager
- Sets read cursor position to 0 in FileManager
- File remains open and accessible
- Enables re-reading file multiple times efficiently
- More efficient than FCLOSE/FOPEN sequence

**Parser Integration** (BasicParser.java lines 413-424):
```java
case FREWIND: {
    _oLogger.debug("-parse-> found Token: <" + _iPosition + "> [FREWIND] ");
    iOrgPosition = _iPosition;
    _iPosition++;
    
    iFileId = Integer.parseInt(consumeToken(BasicTokenType.NUMBER).getText());
    
    aoStatements.add(new FRewindStatement(iOrgPosition, iFileId));
}
break;
```

**Error Handling**:
- Throws RuntimeException if file ID not registered
- Throws RuntimeException if position cannot be set

**Unit Test Coverage**: 9 tests in FRewindStatementTest.java
- Valid file IDs, multiple file IDs
- Invalid file IDs, edge cases (0, -1)
- Large line numbers, empty files
- Large files, multiline content

**Use Case Example**:
```basic
10 FOPEN 1 "report.txt" "r"
20 PRINT "First pass:"
30 GOSUB 100
40 FREWIND 1              ! Go back to start
50 PRINT "Second pass:"
60 GOSUB 100
70 FCLOSE 1
80 END
100 REM Process file
110 WHILE NOT EOF(1)
120     FINPUT 1 LINE$
130     PRINT LINE$
140 WEND
150 RETURN
```

### 11.5 Statement Summary

| Statement | Purpose | Parameters | Implementation Status |
|-----------|---------|------------|----------------------|
| FPEEK | Peek at next character | fileId, variableName | ✅ Complete (May 30, 2026) |
| FPUT | Write character without newline | fileId, expression | ✅ Complete (May 30, 2026) |
| FRENAME | Rename/move file | fileId, newFileName | ✅ Complete (May 30, 2026) |
| FREWIND | Reset file position | fileId | ✅ Complete (May 30, 2026) |

**Total Unit Tests Added**: 38 tests (9+10+10+9)

## 12. Documentation Updates

### 12.1 Files Updated

| File | Updates |
|------|----------|
| `BASIC_CODING_STANDARD.md` | Document file operation functions, error codes |
| `doc/GD-BASIC_Detailed_Design.md` | Add detailed implementation notes for four new statements |
| `README.md` | Mention file operations enhancements |
| `FILE_OPERATIONS_REQUIREMENTS.md` | Add implementation status and statement documentation |

### 12.2 New User Documentation

Create `doc/FILE_OPERATIONS_USER_GUIDE.md`:
- File mode descriptions (read, write, append)
- Error code reference table
- Common patterns (copy file, list directory)
- Performance tips for large files
- Examples of FPEEK, FPUT, FRENAME, FREWIND usage

---

## 12. Success Criteria

- ✅ All 30+ file operations implemented and tested
- ✅ FILEEXISTS, FILEDEL, FILERENAME working
- ✅ Character-level I/O (FGETC, FPUTC) working
- ✅ File seeking (FSEEK, FTELL, FREWIND) working
- ✅ Directory operations (MKDIR, RMDIR, DIRLIST) working
- ✅ Error handling with meaningful error codes
- ✅ Backward compatibility with existing file functions
- ✅ Unit tests covering all operations (90%+ coverage)
- ✅ System tests with real file operations
- ✅ Performance benchmarks meet targets
- ✅ Documentation complete with examples

---

## 13. Implementation Timeline

### Phase 1: Core File Operations (1 week)
- FileManager enhancements
- File existence, size, time operations
- Character-level I/O

### Phase 2: Advanced File Operations (1 week)
- Seeking and positioning
- Binary file support
- Error handling

### Phase 3: Directory Operations (1 week)
- MKDIR, RMDIR, DIRLIST
- Directory traversal
- Path handling

### Phase 4: Testing & Documentation (1 week)
- Unit tests
- System tests
- User guide and examples

---

## Appendix A: File Operation Examples

### A.1 Copy File with Progress
```basic
10 SRC$ = "large.dat"
20 DST$ = "backup.dat"
30 SRC_ID = FOPEN(SRC$, "rb")
40 DST_ID = FOPEN(DST$, "wb")
50 TOTAL = FILESIZE(SRC$)
60 READ_SIZE = 0
70 WHILE NOT EOF(SRC_ID)
80     BUFFER$ = FREAD(SRC_ID, 1024)
90     FWRITE(DST_ID, BUFFER$)
100    READ_SIZE = READ_SIZE + LEN(BUFFER$)
110    PERCENT = (READ_SIZE / TOTAL) * 100
120    PRINT PERCENT; "%"
130 WEND
140 FCLOSE(SRC_ID)
150 FCLOSE(DST_ID)
160 END
```

### A.2 Directory Backup
```basic
10 IF NOT DIREXISTS("backup") THEN MKDIR("backup")
20 FILE_COUNT = DIRLIST(".", "*.txt", FILES$())
30 FOR I% = 1 TO FILE_COUNT
40     SRC$ = FILES$(I%)
50     DST$ = "backup/" + SRC$
60     RESULT = FILECOPY(SRC$, DST$)
70     IF RESULT = 0 THEN PRINT "Copied "; SRC$
80 NEXT I%
90 END
```

### A.3 File Statistics
```basic
10 FILENAME$ = "report.txt"
20 IF NOT FILEEXISTS(FILENAME$) THEN END
30 ID = FOPEN(FILENAME$, "r")
40 LINE_COUNT = 0
50 CHAR_COUNT = 0
60 WHILE NOT EOF(ID)
70     LINE$ = FINPUT(ID)
80     LINE_COUNT = LINE_COUNT + 1
90     CHAR_COUNT = CHAR_COUNT + LEN(LINE$) + 1
100 WEND
110 FCLOSE(ID)
120 PRINT "Lines: "; LINE_COUNT
130 PRINT "Characters: "; CHAR_COUNT
140 END
```

