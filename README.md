# Fetch

## Overview

This Android app fetches data from
the [Fetch Rewards Hiring API](https://fetch-hiring.s3.amazonaws.com/hiring.json), processes it
according to specified requirements, and displays the results in a list format.

Data processing takes place within the repository layer. First items with null or blank `name`
fields are excluded from the list. Then items are sorted by `listId` (ascending) and then by
`name` (ascending).

The UI layer is driven by UI states built within the `HiringViewModel` enforcing a unidirectional
flow of data.

## Design Decisions

- Chose Jetpack Compose for the UI to align with modern Android development practices.
- Implemented a retry mechanism to enhance user experience during network failures.
- Preferred fakes over mocks in repository tests to simulate realistic scenarios.

## Future Improvements

- Implement UI grouping of items by listId with section headers.
- Clarify whether `name` will always be formatted `Item ###`, if so we can improve sorting so for
  example `Item 20` comes before `Item 178`.
