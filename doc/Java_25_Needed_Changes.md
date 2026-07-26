# Java 25 Compliance & Future Changes Guide for GD-BASIC

**Version:** 0.2.0+  
**Current Java Target:** Java 21  
**Target Java Version:** Java 25+  
**Last Updated:** 2026-07-26 10:40 UTC  

---

## Overview

This document outlines potential changes and optimizations needed to upgrade the GD-BASIC interpreter from Java 21 to Java 25+ compliance. The current codebase targets **Java 21** and is stable. This guide serves as a roadmap for future Java version upgrades while the project remains on Java 21 for production use.

---

## Current State

**Current Configuration:**
- Java Version: 21 (LTS)
- Maven Compiler Source/Target: 21
- Status: Stable and production-ready
- Last Migration: June 2025 (Java 8 → Java 21)

---

## Java Version Roadmap

### LTS Versions in Focus
- **Java 21** (Current) — LTS, targeted support until September 2026+
- **Java 25** (Near-term) — Estimated release: September 2024
- **Java 27** (Long-term) — Next LTS candidate

---

## Potential Changes for Java 25+

### 1. Pattern Matching Enhancements

#### Current Status (Java 21)
- Pattern matching for `instanceof` ✅
- Switch patterns ✅
- Record patterns (preview)

#### Java 25 Opportunities
- Enhanced record patterns
- Potential "pattern guards" improvements
- Possible array patterns

**Affected Files:**
- `BasicParser.java` — Switch statement patterns
- `Function.java` — Type dispatch
- `OperatorExpression.java` — Operator pattern matching

**Example Enhancement:**
```java
// Potential Java 25 syntax (hypothetical)
Value result = switch (expression) {
    case IntegerValue(int v) && v > 0 -> new RealValue(v);
    case StringValue(String s) when !s.isEmpty() -> expression;
    default -> throw new SyntaxErrorException("Invalid value");
};
```

### 2. Record Pattern Refinements

#### Current Status (Java 21)
- Basic record patterns supported

#### Java 25 Opportunities
- Nested record pattern matching
- Improved type inference in patterns
- Potential pattern composition

**Potentially Affected Classes:**
- `Token.java` — Could benefit from enhanced record patterns
- `Value` implementations — Pattern matching in operations

### 3. Text Blocks and String Templates

#### Current Status (Java 21)
- Text blocks available
- String templates in preview

#### Java 25 Opportunities
- Full support for string templates (finalized)
- Potential template composition improvements
- Better performance optimizations

**Current Usage in Project:**
```java
// help.txt resource loading (could benefit)
String helpText = """
    GD-BASIC Interactive Line Editor Help
    ...
    """;
```

### 4. Virtual Threads Maturation

#### Current Status (Java 21)
- Virtual threads available and stable

#### Java 25 Opportunities
- Potential performance improvements
- Better integration with structured concurrency
- Refined execution policies

**Potential Improvements:**
- File I/O operations (character-level file reading)
- Multiple file handle management
- Parallel statement execution (if implemented)

### 5. Module System Enhancements

#### Current Status (Java 21)
- Modular system available but not required for GD-BASIC

#### Java 25 Opportunities
- Potential sealed modules
- Enhanced visibility control
- Better encapsulation patterns

**Optional Implementation:**
```java
// Potential module-info.java (optional)
module eu.gricom.basic {
    requires java.base;
    requires commons.cli;
    requires snakeyaml;
    
    exports eu.gricom.basic.api;
    exports eu.gricom.basic.helper to all modules;
}
```

### 6. Collection and Stream Enhancements

#### Current Status (Java 21)
- Modern collections API stable
- Streams finalized

#### Java 25 Opportunities
- Potential stream terminal operations improvements
- Enhanced collection builders
- Better performance characteristics

**Affected Components:**
- `VariableManagement.java` — HashMap/TreeMap usage
- `Function.java` — Array/collection operations
- `BasicParser.java` — List building and manipulation

### 7. Enhanced Exception Handling

#### Current Status (Java 21)
- Record-based exception chains
- Structured concurrency exception handling

#### Java 25 Opportunities
- Improved exception composition
- Potential "exception hierarchies" improvements
- Better error recovery patterns

**Current Exception Hierarchy:**
- `SyntaxErrorException`
- `EmptyProgramException`
- `FileNotFoundException`
- `FileAlreadyExistsException`
- `BasicRuntimeException`

---

## Performance Optimization Opportunities

### 1. Compiler Optimizations

**Potential Improvements:**
- Better escape analysis
- Improved devirtualization
- Enhanced constant folding
- Faster pattern matching compilation

**Testing Needed:**
- Benchmark expression evaluation
- Test parser performance with large programs
- Measure file I/O throughput

### 2. JVM Optimizations

**Potential Enhancements:**
- Generational ZGC improvements
- Better region sampling with G1GC
- Improved class metadata layout
- Enhanced tier-4 compilation thresholds

**Monitoring Points:**
- GC pause times
- Memory footprint
- Startup time

### 3. Memory Layout Improvements

**Java 21+:**
- Compact object headers (potential)
- Value type improvements (future)
- Better array layout for NumericValue types

---

## Security & Stability Considerations

### 1. Dependency Updates

**Current Dependencies (v0.2.0):**
- Apache Commons CLI 1.5.0
- SnakeYAML 2.2
- JUnit 5.10.1

**Java 25 Recommendations:**
- Verify Commons CLI compatibility
- Check SnakeYAML for Java 25+ support
- Update JUnit to latest version (5.11+)

### 2. Security Vulnerabilities

**Monitoring Approach:**
- Regular dependency audits via `mvn dependency:tree`
- Security scanning with OWASP dependency-check
- Review CVE databases quarterly

### 3. Deprecation Tracking

**Current Deprecations (Java 21):**
- Review compiler warnings
- Test with `-Xlint:all` flag
- Update code to avoid deprecated APIs

---

## Code Quality Improvements

### 1. Enhanced Type Safety

**Potential Areas:**
- Sealed classes for Value hierarchy
- Record classes for immutable data
- Pattern matching for type-safe dispatch

**Example (Hypothetical Java 25):**
```java
public sealed interface Value permits IntegerValue, RealValue, StringValue {
    Value evaluate();
}
```

### 2. Better IDE Support

**Benefits:**
- Improved code completion
- Enhanced refactoring support
- Better error diagnostics

### 3. Documentation Generation

**Improvements:**
- Enhanced Javadoc with pattern matching examples
- Better API documentation for embedders
- Version-specific guides for users

---

## Testing Enhancements

### 1. Test Framework Improvements

**Potential Java 25 Features:**
- Enhanced JUnit 5 features
- Better parameterized test support
- Improved test discovery

**Current Test Count:**
- Unit Tests: 1,214+ tests
- System Integration: 34+ tests
- Test Coverage: ~100%

### 2. Performance Testing

**Recommendations:**
- Implement JMH benchmarks for critical paths
- Profile tokenization speed
- Test parser performance with large programs
- Measure runtime execution speed

### 3. Stress Testing

**Areas to Test:**
- Large program files (10K+ lines)
- Deep recursion with GOSUB
- File I/O with large datasets
- Extended variable storage

---

## Migration Path: Java 21 → Java 25+

### Phase 1: Pre-Migration Assessment (2-4 weeks)
1. Document current Java 21 feature usage
2. Audit all dependencies for Java 25 compatibility
3. Run deprecation checks
4. Benchmark current performance baselines

### Phase 2: Dependency Updates (1 week)
1. Update Maven plugins to Java 25+ versions
2. Update third-party dependencies
3. Update build configuration
4. Test builds with new versions

### Phase 3: Code Modernization (4-6 weeks)
1. Apply enhanced pattern matching patterns
2. Update record patterns (if improved)
3. Implement string templates (if finalized)
4. Refactor with new Java features

### Phase 4: Testing & Validation (2-3 weeks)
1. Run full test suite
2. Performance regression testing
3. Security scanning
4. Integration testing

### Phase 5: Documentation (1 week)
1. Update CLAUDE.md with Java 25 requirements
2. Update README with version information
3. Document new feature usage
4. Update user guides

### Phase 6: Release (1 week)
1. Create release branch
2. Generate artifacts
3. Tag release
4. Update CHANGELOG

**Total Estimated Time:** 11-21 weeks (3-5 months)

---

## Backward Compatibility

### Maintaining Java 21 Support

**Recommended Approach:**
- Continue Java 21 as minimum version for v0.2.x series
- Plan Java 25 upgrade for v0.3.0+
- Use feature detection for optional enhancements
- Maintain compatibility layer for tools

### Migration Strategy

```xml
<!-- pom.xml configuration for future upgrade -->
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>

<!-- When ready to upgrade to Java 25:
<maven.compiler.source>25</maven.compiler.source>
<maven.compiler.target>25</maven.compiler.target>
-->
```

---

## Risk Assessment

| Change | Risk Level | Impact | Mitigation |
|--------|-----------|--------|-----------|
| Pattern matching enhancements | Low | Code clarity | Thorough testing |
| String template finalization | Low | Performance | Benchmarking |
| Virtual thread adoption | Medium | Concurrency | Load testing |
| Dependency updates | Medium | Compatibility | Audit dependencies |
| Module system adoption | Medium | Architecture | Gradual adoption |

---

## Decision Points for Future Migration

### Go/No-Go Criteria

**Go for Java 25 upgrade when:**
1. ✅ Java 25 is officially released and stable
2. ✅ All critical dependencies support Java 25
3. ✅ Security advisories for Java 21 become critical
4. ✅ Project timeline allows 3-5 month migration window
5. ✅ Development team is trained on Java 25 features

**No-Go conditions:**
1. ❌ Critical vulnerabilities not yet addressed
2. ❌ Key dependencies not updated
3. ❌ Project in critical maintenance phase
4. ❌ Production stability takes priority
5. ❌ No clear benefits over Java 21

---

## Resource Requirements

### Personnel
- 1 Senior Java Developer: 2-3 days/week for 3-5 months
- 1 QA Engineer: 1-2 days/week for testing
- 1 DevOps Engineer: 1 day/week for CI/CD updates

### Tools & Infrastructure
- Build machine with Java 25 JDK
- CI/CD pipeline updates
- Performance testing infrastructure
- Additional disk space for artifact storage

### Training
- Team training on Java 25 features (1-2 days)
- Documentation review and update
- Code review procedures

---

## Monitoring & Metrics

### Performance Metrics to Track

```
Baseline (Java 21) vs. Target (Java 25):
- Startup time: < 5% increase acceptable
- Memory usage: < 10% increase acceptable  
- Parse speed: ≥ 5% improvement expected
- Execution speed: ≥ 3% improvement expected
- Test suite time: ≤ 10% increase acceptable
```

### Quality Metrics

```
Target for Java 25 migration:
- Test pass rate: 100%
- Code coverage: ≥ 95%
- Zero critical security issues
- Zero deprecation warnings
- Build time: ≤ 30 seconds
```

---

## References

### Java Version Documentation
- [Java 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-relnotes.html)
- [Java Feature Roadmap](https://openjdk.org/projects/jdk)
- [JEP (Java Enhancement Proposals)](https://openjdk.org/jeps/0)

### Project Documentation
- `CLAUDE.md` — Project configuration and build setup
- `README.md` — Version history and feature list
- `doc/GD-BASIC_Detailed_Design.md` — Architecture documentation
- `BASIC_CODING_STANDARD.md` — BASIC language specification

---

## Conclusion

GD-BASIC is well-positioned for future Java version upgrades. The current Java 21 implementation provides:
- Stable, production-ready codebase
- Modern language features utilization
- Comprehensive test coverage
- Clear upgrade path for future versions

The migration to Java 25+ should be undertaken as a planned initiative with proper resource allocation and testing. The benefits (performance, security, maintainability) justify the effort when conditions align.

**Current Status:** ✅ Java 21 Production Ready  
**Next Target:** Java 25+ (when officially released and dependencies updated)  
**Recommended Review Date:** Q3 2025

---

**Document Prepared By:** Claude (AI Assistant)  
**Review Recommended:** Before Java 25 Release  
**Archive Location:** `doc/Java_25_Needed_Changes.md`