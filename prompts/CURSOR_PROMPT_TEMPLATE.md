# Cursor AI Development Prompt Template

This template provides guidelines for AI-assisted development in the GD-BASIC project using Cursor.

## Style Guide Reference

**MANDATORY**: All code must strictly adhere to the style guide defined in `prompts/STYLEGUIDE.md`. This includes:

- Naming conventions (Hungarian notation for member variables, camelCase for methods)
- Code structure and formatting (4-space indentation, brace style, etc.)
- Best practices (getters/setters, error handling, method length)
- Project standards (British English, data formats, version control)

Any discrepancies between the style guide and generated code will require a rewrite of the failed code.

## Functional Requirements Reference

**MANDATORY**: All implementation must align with the functional requirements specified for the feature or task.

### Requirements Documentation
- Reference the functional requirements document or specification for this task
- Ensure all requirements are understood before implementation begins
- Verify that the implementation addresses all specified functional requirements

### Adding Requirements Reference
When using this template, add the functional requirements reference here:

```
<!-- TODO: Add functional requirements reference -->
<!-- Example formats: -->
<!-- - Requirements Document: doc/requirements/feature-name.md -->
<!-- - Issue/Ticket: #123 -->
<!-- - Specification: doc/specs/feature-specification.md -->
<!-- - User Story: As a [user], I want [feature] so that [benefit] -->
```

**Note**: Replace the placeholder above with the actual reference to your functional requirements before starting development.

## Code Quality Requirements

### Experience Level
All code must be written at the level of an **experienced Java programmer**. This means:

- **Design Patterns**: Use appropriate design patterns where they add value
- **SOLID Principles**: Follow SOLID principles for maintainable architecture
- **Performance**: Consider performance implications and optimize where necessary
- **Error Handling**: Implement robust error handling with meaningful messages
- **Code Reusability**: Write reusable, modular code components
- **Best Practices**: Follow Java best practices and idioms

### Code Standards
- Use meaningful variable and method names that are self-documenting
- Keep methods focused and single-purpose (aim for under 50 lines)
- Avoid code duplication through proper abstraction
- Write defensive code that handles edge cases gracefully
- Use appropriate access modifiers (private, protected, public)
- Follow the Hungarian notation convention for member variables as specified in the style guide

## Unit Testing Requirements

**MANDATORY**: All new code must include comprehensive unit test cases.

### Test Coverage Requirements
- **Minimum Coverage**: Aim for 80%+ code coverage
- **Critical Paths**: All public methods must have test cases
- **Edge Cases**: Test boundary conditions, null values, and error scenarios
- **Integration Points**: Test interactions between components

### Test Quality Standards
- Use descriptive test method names: `testMethodName_Scenario_ExpectedResult`
- Follow the Arrange-Act-Assert (AAA) pattern
- Keep tests independent and isolated
- Use appropriate test frameworks (JUnit)
- Mock external dependencies appropriately
- Test both positive and negative scenarios

### Test File Naming
- Test files should follow the pattern: `ClassNameTest.java`
- Place test files in the corresponding test directory structure

## Documentation Requirements

**MANDATORY**: All code must include documentation that enables **junior developers** to understand, work with, and maintain the code.

### Code Documentation Standards

#### Class-Level Documentation
- Include JavaDoc comments for all public classes
- Explain the purpose and responsibility of the class
- Document any design decisions or architectural considerations
- Provide usage examples where helpful

#### Method Documentation
- Include JavaDoc comments for all public and protected methods
- Document parameters with `@param` tags
- Document return values with `@return` tags
- Document exceptions with `@throws` tags
- Explain the method's purpose in simple terms
- Include examples for complex methods

#### Inline Comments
- Use comments to explain **why**, not **what** (code should be self-explanatory)
- Clarify complex algorithms or business logic
- Document non-obvious implementation details
- Explain workarounds or technical debt

### Documentation Style for Junior Developers
- Use clear, simple language
- Avoid jargon unless necessary (and then explain it)
- Provide context and background information
- Include examples and use cases
- Explain the "why" behind decisions
- Document common pitfalls and how to avoid them

### Example Documentation Template

```java
/**
 * Processes user input and validates it against business rules.
 * 
 * This method performs validation on user-provided data to ensure it meets
 * the requirements before further processing. It checks for null values,
 * format compliance, and business rule constraints.
 * 
 * @param strUserInput The user input string to validate. Must not be null.
 * @return true if the input is valid, false otherwise
 * @throws IllegalArgumentException if strUserInput is null
 * 
 * @example
 * // Example usage:
 * if (validateUserInput("user@example.com")) {
 *     processInput("user@example.com");
 * }
 */
public boolean validateUserInput(String strUserInput) {
    // Implementation
}
```

## Development Workflow

1. **Review Functional Requirements**: Understand all functional requirements for the task
2. **Review Style Guide**: Review `Prompts/STYLEGUIDE.md` for coding standards
3. **Plan Implementation**: Consider design patterns and architecture
4. **Write Code**: Follow all style guide rules and quality requirements
5. **Write Tests**: Create comprehensive unit tests alongside code
6. **Document**: Add clear documentation for junior developers
7. **Verify**: Ensure code compiles and all tests pass
8. **Review**: Check adherence to functional requirements, style guide, and quality standards

## Checklist Before Submitting Code

- [ ] Functional requirements are understood and referenced
- [ ] All functional requirements are implemented
- [ ] Code follows all rules in `Prompts/STYLEGUIDE.md`
- [ ] Code quality matches experienced Java programmer standards
- [ ] All public methods have unit tests
- [ ] Test coverage meets minimum requirements
- [ ] All classes and public methods have JavaDoc comments
- [ ] Documentation is clear for junior developers
- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] No code duplication
- [ ] Error handling is implemented appropriately

---

**Remember**: The goal is to produce maintainable, well-tested, and well-documented code that junior developers can understand and work with effectively.
