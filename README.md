
# Financify

## Project Overview

Financify is a comprehensive mobile application designed to empower users with robust financial management tools. It provides an intuitive interface for tracking income and expenses, setting and monitoring saving goals, and gaining insights into personal spending habits. With features like OCR for receipt scanning and detailed transaction categorization, Financify aims to simplify financial management and help users achieve their financial objectives.

## Features

- **Transaction Management:** Easily add, edit, and delete income and expense transactions.
- **Saving Goals:** Create and track personalized saving goals to stay motivated and focused on your financial targets.
- **OCR Receipt Scanning:** Digitize receipts by scanning them with your camera, automatically extracting transaction details.
- **Financial Insights:** Visualize your spending patterns with insightful charts and reports.
- **Secure Authentication:** Protect your financial data with biometric authentication.
- **Data Persistence:** Your financial data is securely stored locally on your device using Room.

## Technologies Used

- **Kotlin:** The primary programming language for the application.
- **Jetpack Compose:** For building the user interface.
- **Room:** For local data storage.
- **Coroutines & Flow:** For asynchronous programming.
- **Retrofit:** For network requests.
- **Coil:** For image loading.
- **ML Kit:** For OCR text recognition.
- **Jetpack Navigation:** For in-app navigation.
- **Paging 3:** For paginating large data sets.
- **Lottie:** For animations.
- **Material Design 3:** For UI components.

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-repository/financify.git
   ```
2. **Open the project in Android Studio.**
3. **Build the project.**
4. **Run the app on an emulator or a physical device.**

## Database Design

The application's database is managed by Room and consists of two main entities: `transactions` and `saving_goals`.

### `transactions` Table

This table stores all the financial transactions recorded by the user.

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

### `saving_goals` Table

This table stores the user's saving goals.

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

