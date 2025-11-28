# Add Transaction Feature Documentation

This document details the architecture and implementation of the **Add Transaction** feature, which covers adding new transactions (Income/Expense), category selection, data persistence, and foundational data access patterns using modern Android development tools.

## 1. Overview and Technology Stack

This feature allows users to quickly save a financial transaction, optionally saving it as a reusable category. It is built using the following components:

| Component | Technology | Role |
| :--- | :--- | :--- |
| **User Interface** | **Jetpack Compose** | Handles the `AddTransactionUi` screen, user input, and state observation. |
| **Business Logic** | **ViewModel** (Jetpack) | Stores and manages UI state, handles form logic, date formatting, and calls to the Repository. |
| **Data Access** | **Repository** | Provides a clean API for the ViewModel, abstracts data sources (DAO). |
| **Persistence** | **Room** (SQLite) | Defines the data access object (`TransactionDao`) for storing and retrieving `Transaction` entity. |

## 2. Architecture and Data Flow

The feature follows the **MVVM (Model-View-ViewModel) pattern** and utilizes Kotlin Coroutines and Jetpack Flow for asynchronous data streams.




1.  **`AddTransactionUi`**: Observes state variables (amount, title, categories, loading) from the **ViewModel** and dispatches user actions (button clicks, input changes).
2.  **`TransactionViewModel`**: Collects `categories` data via `flatMapLatest` from the Repository. On 'Confirm,' it calls `saveTransactionAndGetId()` on the Repository.
3.  **`TransactionRepository`**: Mediates between the ViewModel and the Room database, handling CRUD operations.
4.  **`TransactionDao`**: Defines direct SQLite operations for data insertion, retrieval (including specific queries for categories and date ranges).

## 3. Implementation Details

### 3.1. `AddTransactionUi.kt` (User Interface)

This composable is the primary screen for adding a transaction(Income/Expense).

* **State Management:** Uses a `TransactionViewModel` instance to manage all form fields (`amount`, `title`, `description`, `type`, `selectedDate`, `isCategory`).
* **Categories Quick-Fill:** Displays existing categories relevant to the selected `TransactionType` (Income/Expense) via a `LazyRow`. Clicking a `CategoryCard` fills the form fields instantly.
* **Key Composables:**
    * **`CategoryCard`**: Displays saved categories; provides quick access to fill the form.
    * **`TransactionTypeDropDown`**: Allows selection between `INCOME` and `EXPENSE`.
    * **`InputField`**: A standard `OutlinedTextField` wrapper for all form inputs.
    * **`DatePicker`**: Utilizes `rememberDatePickerState` to handle date selection for the transaction.

### 3.2. `TransactionViewModel.kt` (View Model)

This class manages the UI-related data and logic.

* **Form State:** All input fields are exposed as mutable `State` using Kotlin delegates (`by mutableStateOf`).
* **Category Flow:** The `categories` StateFlow uses `snapshotFlow { type }.flatMapLatest` to dynamically fetch only categories matching the currently selected `type` (Income/Expense), ensuring responsiveness when the user changes the transaction type.
* **Persistence:** The `saveTransactionAndGetId()` function creates a `Transaction` entity from the current form state and delegates the insertion to the Repository.
* **Utility:** Includes helper functions for `formatDate(timestamp: Long)` and `clearForm()`.

### 3.3. `TransactionRepository.kt` (Repository)

The Repository handles data abstraction and introduces Paging 3 logic.

* **Core CRUD:** Methods like `insertTransaction` and `getTransactionById` call directly to the DAO.
* **Paging Implementation:** Uses `Pager` and `PagingConfig` to provide paginated data streams:
    * `getPaginatedTransactions()`
    * `getPaginatedTransactionsByType(type)`
    * `getPaginatedTransactionsOldest()`
* **Page Size:** Pagination is configured with a `pageSize = 20`.

### 3.4. `TransactionDao.kt` (Room Data Access Object)

This interface defines all necessary interactions with the SQLite database via Room annotations.

| Method Name | Query/Action | Purpose |
| :--- | :--- | :--- |
| `insertTransaction` | `@Insert` (REPLACE strategy) | Adds or updates a `Transaction` entry. |
| `getTransactionById` | `@Query` | Retrieves a single transaction by its unique ID. |
| `getTransactions()` | `@Query`, `PagingSource` | Provides all transactions ordered by `date DESC` for Paging 3. |
| `getCategoriesByType` | `@Query("...isCategory = 1 AND type = :type...")` | Retrieves transactions marked as categories, filtered by type. |
| `getTransactionsBetweenDates` | `@Query("...date BETWEEN :startDate AND :endDate...")` | Retrieves transactions within a specific date range. |

## 4. Usage and Integration

### Adding a Transaction

To integrate the transaction screen into the navigation graph:

```kotlin
navController.navigate("transaction/{type}")
```
Where {type} is either INCOME or EXPENSE.


