# Financify Documentation

## 1. Introduction

This document provides a comprehensive overview of the Financify application's architecture, components, and development guidelines. It is intended for developers working on the project, as well as anyone interested in understanding its technical implementation.

## 2. Project Architecture

Financify follows a layered architecture pattern, which separates concerns and promotes a modular, scalable, and maintainable codebase. The main layers are:

- **Presentation Layer:** Responsible for the UI and user interaction.
- **Data Layer:** Responsible for data retrieval and management.
- **DI Layer:** Responsible for dependency injection.

### 2.1. Data Layer

The data layer is responsible for providing data to the application. It abstracts the data sources (local and remote) and exposes a clean API for the rest of the application to consume. This layer is composed of the following components:

- **Data Sources:** These are the classes responsible for retrieving data from a specific source, such as a local database or a remote API. In Financify, we have:
    - **Local Data Source:** The local data source is implemented using the Room persistence library. It provides access to the application's database, which includes the `transactions` and `saving_goals` tables.
    - **Remote Data Source:** Although not currently implemented, the architecture is designed to accommodate a remote data source in the future. This could be a REST API, for example.

- **Repositories:** Repositories are responsible for coordinating data from different data sources. They provide a single source of truth for the application's data.

### 2.2. Presentation Layer

The presentation layer is responsible for displaying the data to the user and handling user interactions. It is composed of the following components:

- **UI (Compose Screens):** The UI is built using Jetpack Compose. Each screen is a composable function that observes data from a ViewModel and displays it to the user.
- **ViewModels:** ViewModels are responsible for holding and managing UI-related data. They expose the data to the UI through `StateFlow` or `LiveData` and handle user interactions by calling the appropriate methods in the data layer.

### 2.3. DI Layer

The DI (Dependency Injection) layer is responsible for providing the dependencies to the different components of the application. Financify uses Hilt for dependency injection.

## 3. Project Structure

The project is organized into the following main packages:

- `com.financify.data`: Contains all the classes related to the data layer, including data sources, repositories, and database entities.
- `com.financify.presentation`: Contains all the classes related to the presentation layer, including Compose screens, ViewModels, and navigation.
- `com.financify.di`: Contains the Hilt modules for dependency injection.

## 4. Database

The application uses a Room database to store the user's financial data. The database consists of two tables: `transactions` and `saving_goals`.

### 4.1. `transactions` Table

| Column            | Type    | Description                                                                          |
| ----------------- | ------- | ------------------------------------------------------------------------------------ |
| `id`              | String  | **Primary Key** - A unique identifier for each transaction (UUID).                     |
| `title`           | String  | The title of the transaction (e.g., "Adobe Illustrator", "Paypal", "Sony Camera").     |
| `amount`          | Double  | The monetary value of the transaction.                                               |
| `type`            | String  | The type of transaction, either `INCOME` or `EXPENSE`.                               |
| `date`            | Long    | The date of the transaction, stored as a Unix timestamp for easy sorting and querying. |
| `description`     | String  | An optional description for the transaction.                                         |
| `receiptImageUrl` | String? | An optional URL for the scanned receipt image.                                       |
| `isCategory`      | Boolean | A flag to indicate if the transaction is a category.                                   |

### 4.2. `saving_goals` Table

| Column         | Type   | Description                                           |
| -------------- | ------ | ----------------------------------------------------- |
| `id`           | String | **Primary Key** - A unique identifier for the saving goal. |
| `userId`       | String | A foreign key to link the goal to a user.             |
| `goalName`     | String | The name of the saving goal.                            |
| `targetAmount` | Double | The target amount to be saved.                          |
| `savedAmount`  | Double | The amount currently saved towards the goal.            |
| `goalType`     | String | The type of the saving goal.                            |
| `selectedDate` | String | The target date for achieving the goal.                 |
| `note`         | String | An optional note for the saving goal.                   |
| `color`        | String | A color code associated with the goal.                  |
| `icon`         | String | An icon associated with the goal.                       |

## 5. Development Guidelines

- **Code Style:** Follow the official Kotlin coding conventions.
- **Testing:** Write unit tests for all ViewModels and use cases. Write instrumentation tests for the UI.
- **Git Workflow:** Use the Gitflow workflow for branching and merging.
