# Cat User Guide

![Cat UI screenshot](Ui.png)

Cat is a **desktop chatbot for managing tasks**. Tell Cat what you need to do and it will keep track of everything for you purr-fectly!

---

## Quick Start

1. Ensure you have Java installed on your computer.
2. Download the latest `cat.jar` from the releases page.
3. Double-click the jar file to launch the app. The GUI should appear in a few seconds.
4. Type a command in the input box and press **Enter** or click **Send**.
5. Refer to the features below for details on each command.

---

## Notes on Command Format

* Words in `UPPER_CASE` are parameters to be supplied by the user.
  e.g. `todo DESCRIPTION` means you type something like `todo buy milk`.
* Date-times must follow the format `yyyy-MM-dd HHmm`.
  e.g. `2025-04-15 1800` means 6:00pm on 15 April 2025.
* Task numbers refer to the index shown when you use `list`.

---

## Command Summary

| Command | Format | Example |
|--------|--------|---------|
| [**todo**](#adding-a-todo-todo) | `todo DESCRIPTION` | `todo Buy groceries` |
| [**deadline**](#adding-a-deadline-deadline) | `deadline DESCRIPTION /by yyyy-MM-dd HHmm` | `deadline Submit report /by 2025-04-15 2359` |
| [**event**](#adding-an-event-event) | `event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm` | `event Meeting /from 2025-04-10 1400 /to 2025-04-10 1600` |
| [**list**](#listing-all-tasks-list) | `list` | |
| [**mark**](#marking-a-task-as-done-mark) | `mark TASK_NUMBER` | `mark 1` |
| [**unmark**](#marking-a-task-as-not-done-unmark) | `unmark TASK_NUMBER` | `unmark 1` |
| [**delete**](#deleting-a-task-delete) | `delete TASK_NUMBER` | `delete 2` |
| [**clear**](#clearing-all-tasks-clear) | `clear` | |
| [**note**](#adding-a-note-to-a-task-note) | `note TASK_NUMBER NOTE_TEXT` | `note 1 bring umbrella` |
| [**find**](#finding-tasks-find) | `find KEYWORD` or `find yyyy-MM-dd` | `find meeting` |
| [**cheer**](#getting-a-cheer-cheer) | `cheer` | |
| [**bye**](#exiting-the-app-bye) | `bye` | |

---

## Features

### Adding a todo: `todo`

Adds a task with no date or deadline.

Format: `todo DESCRIPTION`

Example: `todo Buy groceries from the farmers market`

```
Nya-ice! I've added: [ ] Buy groceries from the farmers market
If I had a can of tuna for every task you have to do, I'd have... 1. Yum!
```

---

### Adding a deadline: `deadline`

Adds a task that must be completed by a specific date and time.

Format: `deadline DESCRIPTION /by yyyy-MM-dd HHmm`

Example: `deadline Submit CS2103T project report /by 2025-04-15 2359`

```
Nya-ice! I've added: [ ] Submit CS2103T project report (by: Apr 15 2025, 11:59pm)
If I had a can of tuna for every task you have to do, I'd have... 2. Yum!
```

---

### Adding an event: `event`

Adds a task that occurs over a time range.

Format: `event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm`

Example: `event Team project meeting /from 2025-04-10 1400 /to 2025-04-10 1600`

```
Nya-ice! I've added: [ ] Team project meeting (from: Apr 10 2025, 2:00pm to: Apr 10 2025, 4:00pm)
If I had a can of tuna for every task you have to do, I'd have... 3. Yum!
```

---

### Listing all tasks: `list`

Shows all tasks currently in your list.

Format: `list`

```
All your work is tiring ME-OWT! Take a look...
1.[T][ ] Buy groceries from the farmers market
2.[D][ ] Submit CS2103T project report (by: Apr 15 2025, 11:59pm)
3.[E][ ] Team project meeting (from: Apr 10 2025, 2:00pm to: Apr 10 2025, 4:00pm)
```

---

### Marking a task as done: `mark`

Marks the specified task as completed.

Format: `mark TASK_NUMBER`

Example: `mark 1`

```
You're pawsitively efficient! This task has been marked as done:
[X] Buy groceries from the farmers market
```

---

### Marking a task as not done: `unmark`

Marks the specified task as not yet completed.

Format: `unmark TASK_NUMBER`

Example: `unmark 1`

```
I was looking forward to a cat nap... but this task is not done yet:
[ ] Buy groceries from the farmers market
```

---

### Deleting a task: `delete`

Removes the specified task from the list.

Format: `delete TASK_NUMBER`

Example: `delete 2`

```
A smart kitty has removed this task:
[ ] Submit CS2103T project report (by: Apr 15 2025, 11:59pm)
If I had a can of tuna for every task you have to do, I'd have... 2. Yum!
```

---

### Clearing all tasks: `clear`

Removes all tasks from the list at once.

Format: `clear`

```
Poof! All 3 task(s) have been swept away. Fresh litter box!
```

---

### Adding a note to a task: `note`

Attaches an optional note to an existing task.

Format: `note TASK_NUMBER NOTE_TEXT`

Example: `note 2 remember to print and sign the cover page`

```
Purr-fect! Note added to task 2:
2.[D][ ] Submit CS2103T project report (by: Apr 15 2025, 11:59pm)
   Note: remember to print and sign the cover page
```

---

### Finding tasks: `find`

Finds tasks matching a keyword or a specific date.

Format: `find KEYWORD` or `find yyyy-MM-dd`

Examples:
* `find meeting` — finds all tasks containing the word "meeting"
* `find 2025-04-15` — finds all deadlines and events on that date

```
Here are the matching tasks in your list:
3.[E][ ] Team project meeting (from: Apr 10 2025, 2:00pm to: Apr 10 2025, 4:00pm)
```

---

### Getting a cheer: `cheer`

Get some encouragement when you're feeling overwhelmed!

Format: `cheer`

---

### Exiting the app: `bye`

Sends a goodbye message and closes the app after a short delay.

Format: `bye`

```
Aww, see mew next time!
```

---

## Saving Your Data

Cat saves your tasks automatically after every command that changes your data. 