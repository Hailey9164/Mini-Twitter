# Mini Twitter
CSC 3560 Assignment 2
A Java Swing application implementing a simplified social media platform using Composite, Observer, Visitor, and Singleton design patterns.

## Overview
This project simulates a miniature version of Twitter, allowing administrators to create users and groups, track system statistics, and open individual user views. Users can follow each other, post tweets, and receive live updates through the Observer pattern.

The system is built around a hierarchical tree structure of users and groups, with a dedicated Admin Control Panel for managing the entire network.

## Features
### Admin Control Panel
- Create new Users
- Create new Groups
- View the entire user/group hierarchy in a JTree
- Open a User View window for any user
- Display system statistics:
  - Total Users
  - Total Groups
  - Total Messages
  - Percentage of Positive Messages

### User View
- Follow other users
- Post tweets
- Live‑updating news feed (Observer pattern)
- Display list of followed users
- Display personal news feed (own tweets + followed users’ tweets)

## System Architecture
### Composite Pattern
Used to represent the hierarchical structure of Users and Groups.
- UserComponent – abstract base class
- User – leaf node
- UserGroup – composite node containing children

This allows the Admin Control Panel to treat users and groups uniformly.

### Observer Pattern
Used to update followers and GUI windows in real time.
- User implements both Subject and Observer
- UserView implements Observer
- When a user posts a tweet:
  - Followers receive updates
  - Any open UserView windows refresh automatically

### Visitor Pattern
Used for computing system statistics.
- StatsVisitor traverses the entire user/group tree
- Counts:
  - Users
  - Groups
  - Tweets
  - Positive tweet percentage

### Singleton Pattern
Ensures only one Admin Control Panel exists.
- AdminControlPanel.instance()
- Private constructor
- Double‑checked locking

## How to Run
1. Compile all .java files:

```bash
javac *.java
```

2. Run the Admin Control Panel:

```bash
java Driver
```

3. Use the GUI to:
- Add users/groups
- Open user views
- Post tweets
- View statistics

## Project Structure

### AdminControlPanel
The central window of the application. It is implemented as a Singleton to ensure only one instance exists. This panel allows the administrator to create users and groups, open user views, and display system statistics. It also manages the JTree that visualizes the entire user/group hierarchy.

### UserView  
A dedicated window for an individual user. It displays the user’s following list and news feed, and provides controls for following other users and posting tweets. UserView observes its associated User object and updates automatically when new tweets arrive.

### UserComponent  
An abstract base class representing any node in the system — either a user or a group. It defines shared behavior and provides the foundation for the Composite pattern.

### User  
Represents an individual user. As a leaf node in the Composite structure, it cannot contain children. Users can follow other users, post tweets, and receive updates through the Observer pattern. Acts as both a Subject (notifying followers and GUI observers) and an Observer (receiving tweets from followed users).

### UserGroup  
Represents a group that can contain both users and other groups. This class implements the Composite pattern by maintaining a list of child components and allowing recursive traversal through the hierarchy.

### Subject  
An interface defining the attach, detach, and notify operations for observers.

### Observer  
An interface implemented by any class that needs to receive updates, including User and UserView.

### Visitor  
An interface defining operations for visiting users and groups.

### StatsVisitor  
Implements the Visitor interface to compute system-wide statistics such as total users, total groups, total tweets, and the percentage of positive messages. It traverses the entire Composite structure, starting from the root group.

### Driver 
Contains the main method and starts the program.

## Design Choices
### 1. Composite for Tree Structure
Users and groups share a common interface, allowing the JTree to display them uniformly.
Groups override getChildren() to return their list, enabling indentation and folder icons.

### 2. Observer for Live Updates
Each user maintains:
- A list of followers (User observers)
- A list of GUI observers (UserView windows)

This ensures:
- Tweets instantly propagate to followers
- All open windows stay in sync

### 3. Visitor for Statistics
Instead of scattering counters across the codebase, the Visitor pattern centralizes all analytics in StatsVisitor.

### 4. Singleton for Admin Panel
Only one Admin Control Panel should exist.
The Singleton pattern prevents accidental duplicates.


# Assignment 3 Updates
This project was expanded with several new functional requirements that improve validation, tracking, and administrative insight. These enhancements build on the existing architecture and integrate smoothly with the Composite, Observer, Visitor, and Singleton patterns already in place.

## ID Validation System
A new button was added to the Admin Control Panel to verify the integrity of all User and Group IDs. When triggered, the system checks two conditions across the entire hierarchy:

- All IDs must be unique
- IDs must not contain spaces

A dialog reports whether the system passes validation or if any issues were detected. This feature helps ensure data consistency without altering or blocking invalid input.

## Creation Time Tracking
Both Users and Groups now store a creationTime attribute, recorded at the moment the object is instantiated. This timestamp is displayed in each User View window, giving administrators and users visibility into when accounts and groups were created. The value is stored in the model layer and rendered in the GUI.

## Last Update Time Tracking
Users now maintain a lastUpdateTime attribute that updates whenever:

- They post a tweet
- They receive a tweet from someone they follow

This allows the system to track the most recent activity across the entire user base. The timestamp is shown in the User View and updates dynamically as the user’s feed changes.

## Find Last Updated User
A new Admin Control Panel button identifies the user with the most recent update based on the lastUpdateTime attribute. When clicked, the system scans all users and displays the ID of the most recently active one. This provides a quick way to monitor engagement and activity trends within the network.

## Author
Hailey Campbell  

CSC 3560 – Object‑Oriented Programming

June 2026
