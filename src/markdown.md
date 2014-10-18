<div class="centered">
<h1>BakaTxt</h1>
Supervisor: <strong>Nirandika Wanigasekara</strong> | Extra feature: <strong>GUI</strong>
<table>
    <thead>
        <th width="25%">
            <img src="http://i.imgur.com/Qjk8utY.jpg" />
            Gil Ki Hyun
        </th>
        <th width="25%">
            <img src="http://i.imgur.com/E15JFc6.png" />
            Cheryl Kee
        </th>
        <th width="25%">
            <img src="http://i.imgur.com/ik3Gzjq.jpg" />
            Bjorn Lim
        </th>
        <th width="25%">
            <img src="http://i.imgur.com/oyf6x54.jpg" />
            Eng De Sheng
        </th>
    </thead>
        <th>
            <div class="no-bold">Team lead, Deliverables and deadlines</div>
        </th>
        <th>
            <div class="no-bold">Scheduling and Tracking, Documentation</div>
        </th>
        <th>
            <div class="no-bold">Testing, Tool X expert</div>
        </th>
        <th>
            <div class="no-bold">Code Quality, Integration, Debugging</div>
        </th>
</table>
</div>
<div class="page-break"></div>

## Contents

- 1. [User Guide](#user)
- 2. [Developer Guide](#developer)
    - 2.1 [Introduction](#intro)
        - 2.1.1 [Audience](#audience)
        - 2.1.2 [Principles](#principles)
    - 2.2 [Design](#design)
        - 2.2.1 [Architecture](#architecture)
        - 2.2.2 [API](#design-api)
        - 2.2.3 [Sequence Diagrams](#sequence)
    - 2.3 [UI](#ui)
        - 2.3.1 [Input](#input)
        - 2.3.2 [Content](#content)
        - 2.3.3 [Feedback Mechanisms](#feedback)
        - 2.3.4 [Other Components](#ui-other)
    - 2.4 [Logic](#logic)
        - 2.4.1 [Executing Commands](#execute)
    - 2.5 [Parser](#parser)
        - 2.5.1 [Date Parsing](#date)
        - 2.5.2 [Parse Sequence](#parse-sequence)
        - 2.5.3 [Task Packaging](#package-task)
    - 2.6 [Database](#database)
        - 2.6.1 [Singleton](#singleton)
        - 2.6.2 [HashMap / LinkedList](#hm-ll)
        - 2.6.3 [Sort](#sort)
        - 2.6.4 [Read / Write](#read-write)
    - 2.7 [Task](#task)
        - 2.7.1 [Output](#output)
        - 2.7.2 [Tags](#tags)
    - 2.8 [Storage File](#storage)
- 3. [Acknowledgements](#acknowledgements)
- 4. [Appendixes](#appendix)
    - 4.1 [Appendix A: User Stories](#user-stories)
        - 4.1.1 [Likely](#stories-likely)
        - 4.1.2 [Unlikely](#stories-unlikely)
    - 4.2 [Appendix B: Product Survey](#product-survey)
        - 4.2.1 [Notational Velocity](#notational-velocity)
        - 4.2.2 [Remember The Milk](#rtm)
        - 4.2.3 [Google Calendar](#g-cal)
        - 4.2.4 [Mac OSX Reminders](#osx-reminders)
        - 4.2.5 [Max OSX Calendar](#osx-cal)
    - 4.3 [Appendix C: API of *BakaTxt*](#api)
    - 4.4 [Appendix D: Example of Storage File](#example-storage-file)

<div class="page-break"></div>

<a name="user"></a>
## 1. User Guide

1. When you first start *BakaTxt*, you will see a prompt asking if you would like the program to be started with Windows. This will only be shown on the first time you run the program.  
<sup>**Note**: You can change your choice in the Options menu.</sup>

2. You will also be prompted to select a key combination to bind with *BakaTxt*. This will also be shown on the first time you run *BakaTxt* and it enables you to quickly access it. The default key combination will be <kbd>CTRL + SHIFT + L</kbd>.  
<sup>**Note**: You can change your choice in the Options menu.</sup>

3. You can add new items into the list with the following command format:  
`add <title> <date> <time> @<venue> --<description>`  
<sup>**Note**: All commands are to be entered without the angle brackets.</sup>  
![](http://i.imgur.com/4PILNBv.png)  
You can add overlapping tasks. This is intentionally done to allow you to manage your tasks hassle-free. 
A *title* is essential and the other details are optional. There must also be a ‘@’ before the *venue* and ‘--’ before the *description*. The positions of *title* and *description* are fixed while positions of the *date*, *time* and *venue* are interchangeable.  
In addition, there is a range of syntax for both *date* and *time* inputs.  
    1. The supported formats for the *date* inputs are:
        1. 3/2/2014, 3/2/14, 3/2
        2. today, tomorrow, next thursday, next month, next year
    2. The supported formats for the *time* inputs are:
        1. 10pm, 7am
        2. 12:59, 23:59, 06:00 hours
        3. noon (12pm), afternoon (12pm), midnight (12am)  
    As for a *time* duration, the supported formats are to include  ‘-’ or ‘to’ between the two timings. For example, 10am to 12pm.

4. You can delete existing items from the list with the following command format:  
`delete <title>`  
A numbered list of tasks containing the same title will be displayed. You can then choose the index to delete with the following command format: `<index>`  
![](http://i.imgur.com/kJaEo45.png)  
As for multiple deletes, you can enter a few index with a space in between. For example, using the command: `1 4 6` will delete tasks with index 1, 4 and 6.

5. You can edit existing items from the list with the following command format:  
`edit <title>`  
Similar to the delete function, a numbered list of tasks containing the same title will be displayed. You can then choose the index to edit with the follow command format: `<index>`  
After choosing the task to edit, the existing *title* will appear in the text box. You can edit the *title* or press enter to move on to the next detail. It will be in the following order: *title*, *time*, *date*, *venue* and lastly, *description*.  
![](http://i.imgur.com/DmQcgKI.png)  
![](http://i.imgur.com/pAuzAcL.png)  

6. You can view existing items from the list with the following command format:  
`display <day/week/month>`  
![](http://i.imgur.com/TPL6fud.png)  
The details (`day`, `week`, `month`) after display represents the current day, week or month when the command is entered. To display all tasks, you can just enter `display`.  
In addition, floating tasks will be displayed first, followed by all day tasks. The remaining tasks will be displayed according to the dates. Within the same day, the tasks will be displayed according to the time.

7. On the Graphic User Interface, the following command format will close the program: `exit`

8. Thank you for using *BakaTxt*. 

<div class="page-break"></div>

<a name="developer"></a>
## 2. Developer Guide

<a name="intro"></a>
### 2.1 Introduction

Welcome to the developer guide for *BakaTxt*. *BakaTxt* is a Java-based to-do manager that allows the User to easily manage their daily tasks. In this guide, you will gain an overview of the basics of development for *BakaTxt*.

<a name="audience"></a>
#### 2.1.1 Audience

This guide is designed for developers with basic knowledge of Java, including the Swing components, as well as object-oriented programming concepts.

<a name="principles"></a>
#### 2.1.2 Principles

*BakaTxt* is designed with simplicity in mind. It is specifically made simple to use, for both developers and the Users. The program is intentionally coded with common basic Java API to ensure better developer compatibility. Further information will be discussed in the following chapters.

---
<a name="design"></a>
### 2.2 Design

<a name="architecture"></a>
#### 2.2.1 Architecture

Following a top-down development approach, you will be first introduced to the architecture of *BakaTxt*. This architecture will form the basis of implementation of *BakaTxt*.  
![](http://i.imgur.com/dTopb42.png)  
<sup>Fig.1. Architecture of *BakaTxt*.</sup>

<a name="design-api"></a>
#### 2.2.2 API

A detailed set of public API for the various components of *BakaTxt* is available in Appendix C. The API is designed in tandem with the architecture to allow for easy extensibility of the features of the `Database`. You should be able to add further features to *BakaTxt* by basing your feature designs around the available API.

<a name="sequence"></a>
#### 2.2.3 Sequence Diagrams

![](http://i.imgur.com/sS0o4s7.png)  
<sup>Fig.2. A sequence diagram depicting the initiation of *BakaTxt*.</sup>  

![](http://i.imgur.com/qGht8Ys.png)  
<sup>Fig.3. A sequence diagram depicting the adding of a `Task`.</sup>

---
<a name="ui"></a>
### 2.3 UI

The UI of *BakaTxt* is mainly made up of many custom drawn `JPanel`s inside a transparent `JFrame`. The `JFrame` is extended by the `BakaUI` class, which has  a `BakaPanel` `JPanel` that holds all of the other drawn objects.  There are 3 main components to the UI: the input, the content, and the feedback mechanisms (to be implemented).  

If you want to update the contents within with new tasks, call the following method from the `BakaUI` class:
```java
public static void updateUI(LinkedList<Task> tasks)
```

<a name="input"></a>
#### 2.3.1 Input

The input, located at the very top of the UI, is the main point of interaction from the User to *BakaTxt*. 

Getting the input (as a `String`) is done from the `BakaUI` class by the method `processInput();`.  `processInput();` uses an `ActionListener()`, which, when activated, should highlight the text in the `Input`box as well as call another method to process the logic requrired for that string.

<a name="content"></a>
#### 2.3.2 Content

The content is the main body of *BakaTxt*. It mainly comprises of two parts, the date and the tasks for that date.

The date is currently just a simple `JTextArea` with formatting. A `FormattedText` class extends the `JTextArea` to simplify matters. Simply call the `FormattedText` constructor to draw the date. Like so: 
```java
new FormattedText(date, UIHelper.FORMAT_DATE);
```
Where `date` is the date in `String` form, and `UIHelper.FORMAT_DATE` specifies the properties of the text, such as it's color and size. 

The tasks are placed in `TaskBox`es, which also in turn use the `FormattedText` class to style it's contents. `TaskBox` follows an abstraction occurance pattern, there are a few types of `TaskBox`es, namely, `FirstTaskBox`, `MiddleTaskBox`, `FinalTaskBox` and `OnlyTaskBox`.  Each of these `TaskBox`es dictate the look and style of the box wherein the task is shown to the User. `FirstTaskBox` is to be used when that task is the first task of the day, but is not the only task the User has for the day. `MiddleTaskBox` is to be used when that task neither the first nor the last task of the day. `FinalTaskBox` is to be used when that task is the last task of the day, but is not the only task the User has for the day. Lastly, `OnlyTaskBox` is to be used when the User only has a single task for the day.

All of these `TaskBox`es can easily be drawn, for example:
```java
new FinalTaskBox(task, backgroundColor);
```

You need to pass the `TaskBox` object a `Task`, as well as a `Color`. In *BakaTxt*, the `Color`s are alternating to distinguish between tasks without being overly fancy.

<a name="feedback"></a>
#### 2.3.3 Feedback Mechanisms

Feedback mechanisms are methods for us to tell the User what the program has done or is unable to do. It's purpose is to help the User ensure that he/she has inputed their commands correctly.

There are currently two proposed feedback mechanisms to be implemented. 

Firstly, the `Input` Box will be made to shake left and right in quick succession to signify that the User has made an error. The main purpose of this is to grab the User's attention. The User may then correct his/her input to rectify the error.

Secondly, in order to inform the User of the type of error he/she has made, there will be a string of text just below the `Input` Box. This string is a way for *BakaTxt* to communicate with the User in order to aid him/her in the usage of *BakaTxt*.

<a name="ui-other"></a>
#### 2.3.4 Other components

Apart from the aforementioned classes, there are a few other helper classes used in the GUI.

The `MouseActions` class allows the User to move the *BakaTxt* window with the mouse from any part of the window. This is important since we have a non-standard UI, i.e., *BakaTxt* lacks a titlebar from which mouse movement is usually performed from.

The `UIHelper` class contains helper methods and variables that are used by many classes in the UI.

---
<a name="logic"></a>
### 2.4 Logic

The logic of *BakaTxt* serves as the main component of the *BakaTxt*, where it interacts with the User through Command Line Interface, Graphic User Interface and also with `Parser` and `Database`. 

`BakaTxtMain` is a very simple class. The class mainly takes in a string of input from the User through the UI. The String of input will be passed to the `executeCommand` method to process the command. 

First, import `BakaUI` to the class:
```java
import bakatxt.gui.BakaUI;
```
Simply call the instance of the linked classes by doing: 
```java
private static BakaParser _parser;
private static Database _database;
_parser = BakaParser.getInstance();
_database = Database.getInstance();
```

<a name="execute"></a>
#### 2.4.1 Executing Commands
The `executeCommand` is a public operation handled by `enum` and `switch` respectively. Define the types of commands in the `enum`:
```java
enum CommandType {
    ADD, DELETE, DISPLAY, CLEAR, DEFAULT, EDIT, EXIT
}
```
Using `switch` statements, you are able to process the different command types with the help of the parser to identify the first word of the `String` input. You can add in or modify the existing command types in the `executeCommand` function by adding a new case or changing the logic of the functions in the different cases.
```java
public static String executeCommand(String input)
```

---
<a name="parser"></a>
### 2.5 Parser

`BakaParser` is the main parsing module of *BakaTxt*. It is responsible for parsing all the inputs from the User, such as getting command word from User inputs. 

<a name="date"></a>
#### 2.5.1 Date Parsing

Currently, the parsing of date and time is done by the Natty Library (http://natty.joestelmach.com). However, as the library follows the American date syntax, some parsing has to be done manually using regex before any Natty usage is possible. The following code shows how the regex is used:
```java
private static final String DATE_FORMAT_DDMMYY_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])[/-](\\d\\d)";
private static final String DATE_FORMAT_DDMMYYYY_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])[/-]((19|2[01])\\d\\d)";
private static final String DATE_FORMAT_DDMM_REGEX = "(0?[12]?[0-9]|3[01])[/-](0?[1-9]|1[012])";
```
The strings matching the regex is then passed into the parser in the format suitable for parsing.

<a name="parse-sequence"></a>
#### 2.5.2 Parse Sequence

`BakaParser` first identifies the description according to the double dash delimiter (“--”), followed by the time and date using the library, then the venue according to the at delimiter (“@”) and lastly the title. 

The position of the description is fixed, hence it is identified first. Time and date is identified next as the positions of date, time and venue are not fixed. After identifying both date and time, venue is parsed next. Any string remaining following the at delimiter (“@”) will be identified as a venue. The title is the last to be identified as it can contain more than a word.  Common prepositions are also removed from the end of the title to account for User inputs such as “add lunch at 1pm”.

<a name="package-task"></a>
#### 2.5.3 Task Packaging

`BakaParser` extracts all of the information and packages them into a Task file. 
```java
Task task = new Task(_title);
task.setDate(_date);
task.setTime(_time);
task.setVenue(_venue);
task.setDescription(_description);
```

---
<a name="database"></a>
### 2.6 Database

The `Database` is the main driver between the logic and the text storage file. It handles reading and writing operations with the storage file. These operations are made available to you in the APIs for *BakaTxt*. 

<a name="singleton"></a>
#### 2.6.1 Singleton

`Database` is a singleton class, which is called by invoking the static method - `getInstance()`. This allows for various classes to share the same instance of the `Database`, allowing memory to be allocated efficiently.

<a name="hm-ll"></a>
#### 2.6.2 HashMap / LinkedList

Information is read from the text storage file and stored in a `HashMap<String, LinkedList<Task>>`. This allows information to be retrieved easily in O(n) time for the specific type of tasks. The combined use of `HashMap` and `LinkedList` is to ensure that *BakaTxt* is able to handle a large volume of tasks without suffering huge performance degradation.

<a name="sort"></a>
#### 2.6.3 Sort

The tasks are sorted based on their keys (dates) in the `Database`. Keeping to *BakaTxt*’s aim of simplicity, the sorting is handled by a simple `Collections.sort()` call to the keys. The follow code snippet shows the sorting mechanism of `Database`.

```java
private void sort() {
  _sortedKeys = new TreeSet<String>(_bakaMap.keySet());
  for (Map.Entry<String, LinkedList<Task>> entry : 
       _bakaMap.entrySet()) {
    LinkedList<Task> today = entry.getValue();
    Collections.sort(today);
  }
}
```
<a name="read-write"></a>
#### 2.6.4 Read / Write

The read and write operations are handled by `BufferedReader` and `BufferedWriter` respectively. This is to allow more efficient IO operations as compared to traditional `FileReader` and `FileWriter`. The tasks are read from the storage files and wrapped by the `Task` class into a `Task` instance.

---
<a name="task"></a>
### 2.7 Task

The Task class is the main wrapper class for information in *BakaTxt*. Each instance contains the vital information of a specific task, as defined by the attributes of the Task class.
```java
private String _title;
private String _date;
private String _time;
private String _venue;
private String _description; 
private boolean _isFloating;
private boolean _isDone;
private boolean _isDeleted;
```
As defined in the API (refer to Appendix C), there are public methods to manipulate the information within the `Task` instance. 

<a name="output"></a>
#### 2.7.1 Output

The `Task` class also contains 2 different forms of `toString()`. `toDisplayString()` formats the `Task` instance to a more User friendly format for displaying in the CLI, whereas `toString()` is utilized by the `Database` class to store the information of the task efficiently in the storage file. The 2 outputs below shows the difference between the 2 different output formats.
<table>
    <thead>
        <th width="50%">
            <code>toDisplayString()</code>
        </th>
        <th width="50%">
            <code>toString()</code>
        </th>
    </thead>
    <tr>
        <td>
            <code>[TITLE] New Task</code>
        </td>
        <td rowspan="7">
            <code>[5000 2014-05-02 2230] [TITLE] New Task [DATE] 2014-05-02 [TIME] 2230 [VENUE] null [DONE] true [FLOATING] false [DELETED] false [DESCRIPTION] null</code>
        </td>
    </tr>
    <tr>
        <td>
            <code>[DATE] 2014-05-02</code>
        </td>
    </tr>
    <tr>
        <td>
            <code>[TIME] 2230</code>
        </td>
    </tr>
    <tr>
        <td>
            <code>[VENUE] null</code>
        </td>
    </tr>
    <tr>
        <td>
            <code>[DESCRIPTION] null</code>
        </td>
    </tr>
    <tr>
        <td>
            <code>[DONE] true</code>
        </td>
    </tr>
    <tr>
        <td>
            <code>[FLOATING] false</code>
        </td>
    </tr>
</table>

<a name="tags"></a>
#### 2.7.2 Tags

Each `Task` contains a tag that it is associated with when stored in the storage file. The tags serve as a marker for sorting and identifying the various kinds of tasks. The list below shows the different tags that can be assigned. The tags are enclosed by square brackets at the start of a task output (`toString()`).

* `0000` : Floating Task
* `5000` : Done Task
* `9999` : Deleted Task
* `<date> <time>` : Scheduled Task

---
<a name="storage"></a>
### 2.8 Storage File

The storage file is designed from ground up to be simple and readable. It has the serve the purpose of efficiency as well as readability for the Users. You are able to get any form of information from the storage file using the Database class. If there is any manual edit in the storage file, these edits will be reflected after *BakaTxt* is restarted.

An example of the storage file is included in Appendix D.

<div class="page-break"></div>

<a name="acknowledgements"></a>
## 3. Acknowledgments

1. [Natty: Natural Language Date Parser](https://github.com/joestelmach/natty)

<div class="page-break"></div>

<a name="appendix"></a>
## 4. Appendixes
<a name="user-stories"></a>
### 4.1 Appendix A: User stories. As a User, I…

<a name="stories-likely"></a>
#### 4.1.1 Likely
ID | I can...(functionality) | so that I...(i.e., value)
---|---|---
commands | use a few variations of the command format | have the flexibility of interacting with the application
undoAction | undo most recent action | make quick changes to my workflow
addTasks | add timed tasks, deadline tasks and tasks with floating tasks | have a single versatile scheduling list
autoComplete | use the auto-complete function | can have faster access to the tasks instead of typing out the whole command
searchTasks | search for tasks | can edit or delete tasks easily
addNotes | add notes to tasks | can append more information to the task
tickTasks | tick tasks that are completed | can see which tasks are completed
addFloating | add all-day tasks or tasks with no deadlines | can add a range of tasks to the list
viewType | view the list in different formats like days/months | can easily view the tasks
notCompleted | be notified that there are uncompleted tasks after the deadline | can choose to reschedule or delete the task
notifyTask | be notified if an added task clashes with other tasks | can be sure that there will not be any clash of events in my schedule
sortPreferences | manually sort the tasks according to my preferences | can plan my tasks around my schedule
snoozeTask | snooze an upcoming or urgent task | will not forget about it | checkTasks | see when the task is added | can have a visual feedback of my schedules

<div class="page-break"></div>

<a name="stories-unlikely"></a>
#### 4.1.2 Unlikely
ID | I can...(functionality) | so that I...(i.e., value)
---|---|---
viewCountdown | see the countdown for tasks with deadlines | can be reminded of the urgent upcoming tasks
markTask | mark the task with my account username | can differentiate the ownership of tasks in a shared list
setRecurring | set recurring tasks on a daily/weekly/fornightly basis | can avoid adding repetitive tasks repeatedly
autoDelete | schedule a task to auto-delete upon deadline | do not have to manually delete the task
prioritizeTask | signify the importance of the tasks | can determine the more urgent tasks to do
colorEntries | see color coded entries | can verify tasks easily
removeAllContaining | remove tasks containing a certain string in one pass | can avoid doing multiple deletes

<div class="page-break"></div>

<a name="product-survey"></a>
### 4.2 Appendix B: Product Survey

<a name="notational-velocity"></a>
#### 4.2.1 Notational Velocity  
**Documented by**: Eng De Sheng

**Strengths**:
- Able to issue commands through keyboard
- ‘one-shot’ approach: able to type in commands in one line
- Able to capture to-do items without a specific deadline
- No need for Internet connectivity
- Able to start with a shortcut key
- Power Search
- Mouse gestures not required, strictly optional
- Able to start application with a shortcut key

**Weaknesses**:
- No tracking of to-do items on list
- No scheduling features of any form
- Unable to archive done to-do items

<a name="rtm"></a>
#### 4.2.2 Remember The Milk (RTM)  
**Documented by**: Eng De Sheng

**Strengths**:
- Tracks to-do items
- Helps User to decide what and when to-do items
- Able to schedule to-do items before or after a specific time
- Able to capture to-do items without a specific deadline
- No need for Internet connectivity
- Able to find a suitable slot to schedule a to-do item
- Able to archive a to-do item
- Able to postpone a to-do item
- Easy way to block and release multiple time slots for tentative to-do items

**Weaknesses**:
- Unable to start application with a shortcut key
- Unable to type in commands in one line
- Unable to issue commands with keyboard
- Mouse gestures are required

<a name="g-cal"></a>
#### 4.2.3 Google Calendar  
**Documented by**: Cheryl Kee

**Strengths**:
- Able to track to do items
- Allows the User to view when are the free slots to add items
- Able to display to do items that need to be done before a specific time and without a specific date
- Able to add an item with details in one line
- Able to mark an item as done
- Able to decide what to do item to do next

**Weaknesses**:
- Unable to issue commands using keyboard
- Needs internet connectivity 
- Unable to postpone an item
- Unable to block multiple slots when confirming the time for an item
- Unable to start application with a shortcut key

<a name="osx-reminders"></a>
#### 4.2.4 Mac OSX Reminders  
**Documented by**: Gil Ki Hyun

**Strengths**:
- Simple GUI
- Under a page, you can add list of things to do by its titles
- Able to specify a date to be completed by
- There is a function to remind the User on a particular time, or at a particular location
- The User can set the priority of the task from low to high and setting it to a low will add a ! mark in front of the task list and three ! mark for high one.
- The User also can add additional information under the notes, which will be added below the title.
- Once a User sets a due date for the task, you are able to view using the calender function to check each day.
- Able to sort by due date, priority, creation date or the title

**Weaknesses**:
- Firstly, it depends too much on the mouse
- Must manually delete the task by deleting the title (can be a little troublesome)

<a name="osx-cal"></a>
#### 4.2.5 Mac OSX Calendar  
**Documented by**: Bjorn Lim

**Strengths**:
- Very clean and simple
- Easily able to see what is happening at what time
- Able to see what is currently happening
- Can set recurring events
- No need internet connectivity
- Can see daily/weekly/monthly etc view
- Multiple events at the same time

**Weaknesses**:
- Not keyboard friendly at all
- Cannot set deadlines
- Only one type of entry allowed, “events”

<div class="page-break"></div>

<a name="api"></a>
### 4.3 Appendix C : API of *BakaTxt*

<table>
    <thead>
        <th>
            class
        </th>
        <th>
            API
        </th>
    </thead>
    <tr>
        <td>
            <code>BakaTxtMain</code>
        </td>
        <td>
            <code>executeCommand(String) : String</code><br />
        </td>
    </tr>
    <tr>
        <td>
            <code>BakaParser</code>
        </td>
        <td>
            <code>getCommand(String) : String</code><br />
            <code>add(String) : Task</code><br />
            <code>getString(String) : String</code><br />
        </td>
    </tr>
    <tr>
        <td>
            <code>Database</code>
        </td>
        <td>
            <code>setFile(String) : String</code><br />
            <code>getFileName(): String</code><br />
            <code>add(Task) : boolean</code><br />
            <code>delete(Task) : boolean</code><br />
            <code>setDone(Task, boolean) : boolean</code><br />
            <code>setFloating(Task, boolean) : boolean</code><br />
            <code>getTaskWithTitle(String) : LinkedList<Task></code><br />
            <code>getTaskWithDate(String) : LinkedList<Task></code><br />
            <code>getAllTasks() : LinkedList<Task></code><br />
            <code>getAllUndoneTasks() : LinkedList<Task></code><br />
            <code>remove</code><br />
            <code>close() : void</code><br />
        </td>
    </tr>
    <tr>
        <td>
            <code>Task</code>
        </td>
        <td>
            <code>getTitle() : String</code><br />
            <code>getDate() : String</code><br />
            <code>getVenue() : String</code><br />
            <code>getDescription() : String</code><br />
            <code>setTitle(String) : String</code><br />
            <code>setDate(String) : String</code><br />
            <code>setTime(String) : String</code><br />
            <code>setVenue(String) : String</code><br />
            <code>isDone() : boolean</code><br />
            <code>isFloating() : boolean</code><br />
            <code>isDeleted() : boolean</code><br />
            <code>setDone(boolean) : void</code><br />
            <code>setDeleted(boolean) : void</code><br />
            <code>setFloating(boolean) : void</code><br />
            <code>toDisplayString() : String</code><br />
            <code>getKey() : String</code><br />
        </td>
    </tr>
    <tr>
        <td>
            <code>BakaUI</code>
        </td>
        <td>
            <code>StartGui() : void</code><br />
            <code>processInput() : void</code><br />
            <code>getInput() : String</code><br />
            <code>updateUI() : void</code><br />
        </td>
    </tr>
</table>

<div class="page-break"></div>

<a name="example-storage-file"></a>
### 4.4 Appendix D : Example of Storage File

```
[-] BakaTxt Human-Readable Human-Fixable Database
[-] alpha v0.0
[-] Each task has to be in the same line

[2014-10-14 0900] [TITLE] this is a task [DATE] 2014-10-14 [TIME] 0900 [VENUE] computing [DONE] false [FLOATING] false [DELETED] false [DESCRIPTION] descriptions! 
```