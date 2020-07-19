# LoginApp

[![License](https://img.shields.io/github/license/IkariMeister/LoginApp.svg?style=flat-square)](LICENSE)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![Android CI](https://github.com/Ikarimeister/LoginApp/workflows/Android%20CI/badge.svg)](https://github.com/Ikarimeister/LoginApp/actions)
[![Build Status](https://travis-ci.com/IkariMeister/LoginApp.svg?branch=master)](https://travis-ci.com/IkariMeister/LoginApp)

Simple login 

## Getting Started

This repository contains an Android application that allows user to perform a log in and log out. The login will be persisted, so the user won't have to type their credentials every time the app will open.

![UIpreview](docs/UIpreview.png) 

## CI

Several CI checks are set up yo guarantee code style and code quality standards are fulfilled.

* **_Android Checkstyle_**: this GitHub Action will check the code style on every push and pull request by using **ktlint** and **detekt**
* **_Android CI_**: this GitHub Action will run unit tests on every push and pull request.
* **_labeler_**: GitHub Action to check on pull request, the size of the pull request categorizing them in sizes according to the number of lines modified. It will fail if the pull request has more than 1000 lines modified.
* **_Travis CI_**: TravisCI will be used on pull requests to run heavier tests, like UI tests, to not delay checks on pushes.


