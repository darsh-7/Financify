# Savings Goals Architecture: Presentation Layer Integration

---

## Overview

This document details the final integration of the **Data Layer** (Room/Repository) with the **Presentation Layer** (Jetpack Compose/ViewModel) for the Savings Goals feature. The `SavingsListScreen` Composable serves as the entry point, responsible for initializing dependencies and observing the necessary state flows from the `SavingGoalViewModel`. The architecture strictly follows the **MVVM** (Model-View-ViewModel) pattern.

---

## Presentation Layer: `SavingsListScreen.kt`

The `SavingsListScreen` Composable is the root of the Savings Goals view. Its primary responsibility is not rendering UI elements directly, but rather **dependency setup and state observation**.

### 1. Dependency Initialization

The screen handles the creation and scoping of the core dependencies necessary for the application to function.

* **Database:** The `AppDatabase` instance is retrieved using `AppDatabase.getDatabase(context)` and wrapped in `remember` to ensure it's initialized only once.
* **Repository:** The `SavingGoalRepository` is created, injecting the database's `savingGoalDao()`. It is also wrapped in `remember`.
* **ViewModel:** The `SavingGoalViewModel` is instantiated using the custom `SavingGoalViewModelFactory`. This factory is crucial for injecting the required `Repository` into the ViewModel, adhering to Dependency Inversion Principles (DIP).

### 2. State Observation (Flow Integration)

The screen collects the continuous data flows from the ViewModel and translates them into Compose's reactive state.

* **Goals List:** `viewModel.allGoals` (**StateFlow**) is collected using `.collectAsState()` into the Compose state variable `goalsList`. This state holds the entire list of `SavingGoal` objects.
* **Statistics:** `viewModel.totalStats` (**StateFlow**) is collected into the state variable `stats`.

The use of **`collectAsState()`** ensures that whenever the underlying Room database updates (triggering a new emission from the Flow), the UI automatically recomposes (re-renders) with the latest data.

### 3. UI Structure (`Scaffold`)

The screen utilizes the Material 3 `Scaffold` component for a standard layout and structure.

* **Top Bar:** `GoalListTopBar` handles navigation and primary screen actions.
* **Content:** The main content is delegated to the `SavingsListContent` composable. This content view receives the necessary data (`goalsList`) and interaction hooks (`viewModel`) to render the list and handle user input.

---

## Unidirectional Data Flow (UDF)

The data flows in a single direction, ensuring predictability and maintainability:

1.  **DB/Repository:** The DAO emits changes via **Flow**.
2.  **ViewModel:** The ViewModel uses **`stateIn`** to convert the Room Flow into an observable **StateFlow**, managing the ongoing state.
3.  **UI/Compose:** The `SavingsListScreen` collects the StateFlow using **`collectAsState()`**.
4.  **User Action:** User interacts (e.g., adds a goal).
5.  **ViewModel Action:** `viewModel.saveGoal()` is called, triggering a DB write operation.
6.  **Cycle Restart:** The DB update triggers Room's Flow, restarting the cycle from Step 1 with the new data.

This strict UDF cycle guarantees that the UI always reflects the current state of the database.
##  Analysis Screen Module Documentation

The Analysis Screen is the core reporting module of Financify, providing users with reactive, real-time insights into their spending habits and savings progress over customizable time periods.

### 1. Data Logic: `AnalysisViewModel`

The analysis logic is implemented within the **`AnalysisViewModel`**, which is responsible for fetching, processing, and presenting financial data reactively using **Kotlin Flows** and **Coroutines**.

#### Reactive Data Pipeline

The central mechanism is the `monthlyStats` flow, which ensures data consistency and efficiency:

* **Customizable Period:** The user sets the number of months (`_selectedMonths`) they wish to analyze.
* **`flatMapLatest`:** This crucial operator ensures that if the user changes the month selection rapidly (e.g., from 3 months to 6 months), the previous, slower database query is **cancelled**, and a new query is immediately initiated for the latest selection. This prevents resource waste and avoids displaying stale data.
* **Data Aggregation:** The `computeMonthlyStats` function performs the heavy lifting:
    1.  It uses **`Calendar`** logic to group raw `Transaction` entities by `Year-Month`.
    2.  It calculates the sum of `INCOME` and `EXPENSE` amounts for each group.
    3.  It converts the raw data into a list of structured **`MonthlyStats`** objects, ready for charting.

#### Key Functions

| Function | Purpose |
| :--- | :--- |
| `setMonths(Int)` | Updates the flow trigger, initiating a fresh data fetch and calculation. |
| `computePeriod(Int)` | Calculates the `startDate` and `endDate` timestamps (Long) required for the database query. |
| `monthlyStats` | The final **`StateFlow`** that the UI consumes, containing processed and summarized data. |

### 2. UI Presentation: `AnalysisScreen`

The screen is built with **Jetpack Compose** and is divided into two primary tabs, managed by the **`SegmentedTab`** component:

#### A. Income vs Expense Tab (`IncomeExpenseTab`)

This tab focuses on the transaction data fetched by the `AnalysisViewModel`.

* **Total Calculations:** It calculates three key metrics from the aggregated `MonthlyStats`:
    * **Total Income**
    * **Total Expense**
    * **Net Income** (`Total Income - Total Expense`)
* **Data Visualization:** It displays the monthly income and expense figures using a Chart component (using the extension function `.toBarData()`).
* **Alerts and Messaging:** The **`AnalysisMessageCard`** provides personalized feedback based on the **`Net Income`**:
    * **Positive Net Income:** Displays a congratulatory savings message (Green card).
    * **Negative Net Income:** Displays an alert, specifically listing the months where expenses exceeded income (Red card).
* **Call to Action:** Includes a button to navigate to the full Transaction List for deeper review.

#### B. Savings Analysis Tab (`SavingsAnalysisTab`)

This tab integrates data from the separate `SavingGoalViewModel` to show the user's progress on their financial goals, including completion stats and overall funding status.

### 3. Data Source Layer

The data retrieval is handled by the **`TransactionRepository`** which calls the **`TransactionDao`** using a Room Query:

```kotlin
// Inside TransactionDao
@Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
fun getTransactionsBetweenDates(startDate: Long, endDate: Long): Flow<List<Transaction>>
