# LoginApp

[![License](https://img.shields.io/github/license/IkariMeister/LoginApp.svg?style=flat-square)](LICENSE)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![Android CI](https://github.com/Ikarimeister/LoginApp/workflows/Android%20CI/badge.svg)](https://github.com/Ikarimeister/LoginApp/actions)
[![Build Status](https://travis-ci.com/IkariMeister/LoginApp.svg?branch=master)](https://travis-ci.com/IkariMeister/LoginApp)

Simple login 

## Getting Started

This repository contains an Android application that allows user to perform a log in and log out. The login will be persisted, so the user won't have to type their credentials every time the app will open.

![UIpreview](docs/UIpreview.png) 

Since the API is not ready a Fake implementation of the API client will be provided. As the contract of the API is already defined, We will implement also a Real **ApiClient**, using retrofit, that will be tested using **HttpStubbing** with _MockWebServer_.

The local storage for the login information is implemented using a **Repository** pattern. With the requirements we already have, the data source we will use will be Android Shared Preferences since is a simple solution, but in the future could be replaced by a database implementation, to make this change easier we will use Repository pattern, to abstract the business logic from the real data source and allow to replace data source implementation fast and smooth.
Since the data source is implemented with `Shared Preferences` over Android SDK, instrumentation tests will be required to test its correct working. The error hierarchy is:

```kotlin
Either<StorageError, Token>

sealed class StorageError
object TokenNotFound : StorageError()
data class UnknownStorageError(val t: Throwable) : StorageError()
```
For the **domain layer**, **Command** pattern has been used as use cases. They are responsible to execute all business logic. Login use case has his own hierarchy error like this:

```kotlin
Either<LoginError, Token>

sealed class LoginError

object NoConection : LoginError()
object IncorrectCredentials : LoginError()
```

For the threading problem, `kotlinx.Coroutines` are the solution chosen as **Interactors** since they are a fancy and most common way to implement the Interactors nowadays.

For the presentation layer, **MVP** pattern is the chosen implementation because is the most familiar implementation for the development team, we could consider moving to **MVVM** with data binding, but our expertise and confidence with MVP make us feel more comfortable.

`Koin` has been used as a Dependency provider, thought `Koin` is not a **DI framework**, it fits well with providing the needs of this project by now as Service Locator. If the project scales to much migrating to real DI frameworks like `Kodein` or `Dagger` would be recommended but `Koin simplicity benefits are enough to use it at this time.

User validation has been implemented by using `ValidatedNel` applicative from `Arrow` library. The validation process will be accumulative and will show all errors found.

Rules that have been implemented are:
* Email length less than 256 characters,
* Email should contain '@'
* Email should match a pattern.
* Password length greater or equals than 4 characters
* Password length less than 16 characters.

To add more rules just add an extra function on the corresponding type to validate like this: 

```kotlin
fun validate() =
            Validated.applicative(NonEmptyList.semigroup<ValidationErrors>()).mapN(
                    this.validate(NotAnEmail) { this.value.contains("@") },
                    this.validate(TooLongEmail) { this.value.length < maxLength },
                    this.validate(NotValidCharsInEmail) { this.value.matches(EMAIL_REGEX.toRegex()) }
            )
```
The first function parameter should be an error in the hierarchy. The second parameter is a lambda with the condition to satisfy. 

The validation error hierarchy is:
```kotlin
sealed class ValidationErrors

sealed class EmailValidationErrors : ValidationErrors()
sealed class PasswordValidationErrors : ValidationErrors()

object TooShortPassword : PasswordValidationErrors()
object TooLongPassword : PasswordValidationErrors()
object NotAnEmail : EmailValidationErrors()
object NotValidCharsInEmail : EmailValidationErrors()
object TooLongEmail : EmailValidationErrors()
```

## CI

Several CI checks are set up yo guarantee code style and code quality standards are fulfilled.

* **_Android Checkstyle and Unit Test_**: this GitHub Action will check the code style on every push and pull request by using **ktlint** and **detekt**. It also run Unit tests. The report can be found as an artifact for this action.
* **_Android CI_**: this GitHub Action will assemble the debug binaries on every pull request. Those binaries can be found as binaries artifact on the action.
* **_labeler_**: GitHub Action to check on pull request, the size of the pull request categorizing them in sizes according to the number of lines modified. It will fail if the pull request has more than 1000 lines modified.
* **_Travis CI_**: TravisCI will be used on pull requests to run heavier tests, like UI tests, to not delay checks on pushes.


