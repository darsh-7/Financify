# Text Recognition & AI Receipt Analysis

This documentation details the technical implementation of the automated receipt scanning feature in **Financify**. This feature leverages on-device Optical Character Recognition (OCR) and cloud-based Generative AI to transform physical receipt images into structured transaction data.

## 1. Overview

The Text Recognition service streamlines the user experience by automating data entry.
**User Flow:**
1.  User opens the **Text Recognition Screen**.
2.  User captures a photo of a receipt.
3.  **OCR Service** extracts raw text from the image.
4.  **AI Service** analyzes the text to identify semantic fields (Title, Amount, Date, Category).
5.  User is navigated to the **Add Transaction Screen** with the form pre-filled.

## 2. Architecture

The feature is built using the **MVVM (Model-View-ViewModel)** pattern, ensuring separation of concerns between UI rendering, business logic, and data processing.

### 2.1. Key Components

| Component | Class | Responsibility |
| :--- | :--- | :--- |
| **UI Layer** | `TextRecognitionScreen.kt` | Renders the Camera preview and handles image capture interactions. |
| **Business Logic** | `TransactionViewModel.kt` | Orchestrates the OCR and AI pipeline; manages UI state (`loading`, `parsing`, `result`). |
| **OCR Engine** | `ML Kit Text Recognition` | On-device extraction of text from `ImageProxy`. |
| **AI Engine** | `Google Gemini` | Large Language Model (LLM) used to parse unstructured text into JSON. |

---

## 3. Implementation Details

### 3.1. Image Capture (`TextRecognitionScreen.kt`)
The screen utilizes **CameraX** for a robust camera experience.
-   **Preview**: `LifecycleCameraController` binds the camera lifecycle to the composable.
-   **Capture**: The `takePicture` method retrieves an in-memory `ImageProxy`.
-   **State Observation**: The UI observes `isOcrLoading` and `isParsing` from the ViewModel to display a `CircularProgressIndicator` while processing.

### 3.2. The Processing Pipeline (`TransactionViewModel.kt`)

The core logic resides in `TransactionViewModel`. This shared ViewModel approach allows the data extracted in the recognition phase to be immediately available in the transaction entry phase without complex argument passing.

#### Stage 1: Optical Character Recognition (OCR)
*   **Library**: `com.google.mlkit:text-recognition`
*   **Input**: `ImageProxy` from CameraX.
*   **Method**: `processImage(imageProxy: ImageProxy)`
*   **Logic**:
    1.  Converts `ImageProxy` to `InputImage`.
    2.  Passes image to `TextRecognition.getClient()`.
    3.  On success, passes the raw string to the analysis stage.
    4.  Ensures `imageProxy` is closed to prevent memory leaks.

#### Stage 2: AI Analysis
*   **Library**: `com.google.ai.client.generativeai`
*   **Model**: `gemini-flash-lite-latest`
*   **Method**: `analyzeReceipt(text: String)`
*   **Prompt Engineering**:
    The system sends a structured prompt to the LLM to enforce a JSON output format:
    > *"Analyze the following receipt text and extract the title, amount, category, date, and a brief description. Return the data in JSON format..."*

#### Stage 3: Parsing & State Update
*   **JSON Parsing**: The raw response from Gemini is parsed using **GSON**.
*   **Error Handling**: Includes `try-catch` blocks for `JsonSyntaxException` to handle cases where the AI response is malformed.
*   **State Mapping**:
    *   `title` -> Extracted Vendor/Store Name
    *   `amount` -> Extracted Total
    *   `date` -> Normalized Timestamp (or current time fallback)
    *   `type` -> Defaults to `EXPENSE`

### 3.3. Navigation

Upon successful parsing, the `TextRecognitionScreen` triggers a navigation event:
```kotlin
navController.navigate("transaction/${TransactionType.EXPENSE.name}")
```
Since `TransactionViewModel` is scoped or shared, the destination screen reads the pre-filled state variables (`title`, `amount`, etc.) directly.

---

## 4. Legacy & Refactoring Notes

### `TextRecognitionResultScreen.kt`
*   **Status**: **Deprecated / Unused**.
*   **Reason**: Initially designed to show an intermediate result verification step. The workflow was optimized to navigate directly to the main `AddTransactionScreen`, which already serves the purpose of verification and editing. This reduces code duplication and provides a seamless user flow.
*   **Code Reference**: The file currently contains commented-out Compose code for a result view.

### `TextRecognitionViewModel.kt`
*   **Status**: **Deprecated / Unused**.
*   **Reason**: Logic was merged into `TransactionViewModel` to simplify state sharing between the scanning and editing phases.

---

## 5. Dependencies

Ensure the following dependencies are present in `build.gradle`:

```groovy
// CameraX
implementation "androidx.camera:camera-camera2:1.3.x"
implementation "androidx.camera:camera-lifecycle:1.3.x"
implementation "androidx.camera:camera-view:1.3.x"

// Google ML Kit
implementation "com.google.mlkit:text-recognition:16.0.0"

// Google AI (Gemini)
implementation "com.google.ai.client.generativeai:generativeai:0.x.x"

// GSON
implementation "com.google.code.gson:gson:2.10.1"
```
