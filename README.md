# Online Library Application

A JavaFX-based application that provides users with an easy and efficient way to manage their interactions with an online library.

## Features

- **Borrow Books**: Users can borrow books from the library's collection.
- **Return Books**: Manage borrowed books by returning them when finished.
- **View Book Details**: Access detailed information about each book, including title, author, and description.
- **Search for Books**: Quickly find books by their names using the search functionality.

## Technology Stack

- **Frontend**: JavaFX
- **Backend**: Java
- **Database**: SQlite
## How to Run the Application

### Prerequisites
1. **Java Development Kit (JDK)**: Ensure JDK 11 or higher is installed. [Download JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
2. **JavaFX Libraries**: If your JDK does not include JavaFX, download and configure it:
   - [JavaFX Download](https://openjfx.io/)
3. **IDE**: Install an IDE like IntelliJ IDEA, Eclipse, or NetBeans that supports JavaFX projects.

### Steps to Run
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/S0KPiseth/Online-Library-application.git
2. **Open the Project in Your IDE**:
    - **Import the project into your IDE as a Java project**.
    - **Ensure all dependencies are correctly configured (e.g., JavaFX library)**.
3. **Set Up JavaFX**:
   If your IDE requires manual configuration of JavaFX, follow these steps:
     - Add the JavaFX library to your project
     - Set the VM options for JavaFX (replace ```[path-to-javafx-lib]``` with the actual path to your JavaFX SDK lib directory):
       ```bash
       --module-path [path-to-javafx-lib] --add-modules javafx.controls,javafx.fxml
4. **Run the Application**:
     - Navigate to the entry point of the application:
       ```bash
       Online-Library-application/src/main/java/ui/libraryui/MainUIapp.java
       ```
     - Locate the ```MainUIapp``` class in your IDE.
     - Run the ```MainUIapp``` class to start the application.
  
### Using the Application:
  - Launch the application window.
  - Use the search bar to find books.
  - Click a book to view its details.
  - Borrow or return books using the respective options.

# Limitations

1. **Real-Time Data Fetching**:  
   The application fetches book data in real time. This may occasionally cause delays, especially with a slow or unstable network connection.

2. **Lack of Loading Screens**:  
   The absence of loading indicators during data fetching can make the app feel unresponsive, potentially causing inconvenience to users.
# How to Contribute

We welcome contributions to improve the Online Library Application! Here's how you can help:

### 1. **Bug Fixes**
   If you find a bug, follow these steps:
   - **Reproduce the Issue**: Try to reproduce the issue to understand its behavior.
   - **Create a Fix**: Work on fixing the bug in your local branch.
   - **Test**: Verify that your fix works and doesn’t break anything else in the app.
   - **Submit a Pull Request**: Once the fix is ready, submit a pull request with a clear description of the bug and the fix.

### 2. **Bug Reports**
   If you encounter a bug, please report it:
   - **Provide a Clear Description**: Describe the bug in detail, including what you expected to happen and what actually happened.
   - **Steps to Reproduce**: Provide a step-by-step guide on how to reproduce the issue.
   - **Screenshots/Logs**: Include any relevant screenshots or error logs to help us understand the issue better.

   You can open a new issue on GitHub under the [Issues](https://github.com/S0KPiseth/Online-Library-application/issues) section of the repository, and we’ll address it as soon as possible.

### 3. **Feature Suggestions**
   If you have an idea for a new feature, we’d love to hear it:
   - **Describe the Feature**: Provide a detailed description of the feature and how it could improve the app.
   - **Use Cases**: Explain how this feature will be useful for users.
   - **Submit a Feature Request**: Open an issue in the "Feature Requests" section of GitHub with your suggestion. We’ll review it and consider adding it in a future update.

By following these steps, you can help us improve the app and provide a better experience for all users. Thank you for contributing!



