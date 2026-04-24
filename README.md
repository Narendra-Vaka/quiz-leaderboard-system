# quiz-leaderboard-system
#  Quiz Leaderboard System

##  Overview
This project is part of the Bajaj Finserv Health Internship Assignment.

The application fetches quiz data from an API, removes duplicate entries, calculates total scores for each participant, generates a leaderboard, and submits the result.

---

##  Objective
- Poll API 10 times
- Handle duplicate data
- Aggregate scores
- Generate leaderboard
- Submit final result

---

##  Tech Stack
- Java
- HttpURLConnection
- org.json library

---

##  Workflow

1. Call API 10 times using poll values 0–9  
2. Maintain 5-second delay between each call  
3. Collect all responses  
4. Remove duplicates using:
   roundId + participant
5. Calculate total score per participant  
6. Sort leaderboard in descending order  
7. Submit final result  

---

##  Key Logic

### Deduplication
Each entry is uniquely identified by:
roundId + "_" + participant

Duplicate entries are ignored.

---
