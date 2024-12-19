
## 🔍 **Overview**

This project focuses on implementing solutions for **data processing, storage, and analysis**. The problems solved include:

1. **Problem 1A**: Processing and transforming Reuters News data and storing it in MongoDB.  
2. **Problem 1B**: Performing word frequency analysis using Apache Spark.  
3. **Problem 3**: Conducting sentiment analysis on article titles using a Bag-of-Words (BOW) model.  

---

## 🛠️ **Technologies Used**
The following technologies and tools were utilized in this project:

- **MongoDB Atlas** & **MongoDB Compass** 📦 - For data storage and management.  
- **Apache Spark**  - For distributed word frequency analysis.  
- **Java**  - For backend processing and implementation.  
- **Regex**  - For data extraction and text cleaning.  
- **CSV**  - To store and view the final outputs.  

---

## 🧩 **Problem Descriptions**

### ✅ **Problem 1A: Reuters News Data Processing & MongoDB Storage**
- Reads **Reuters SGM files**.
- Extracts **article titles** and **bodies** using **Regex**.
- Cleans text to **remove special characters**.
- Inserts the processed articles into a **MongoDB** collection:  
  - **Database**: `ReuterDb`  
  - **Collection**: `articles`

---

### ✅ **Problem 1B: Word Frequency Analysis Using Apache Spark**
- Reads **Reuters SGM files**.
- Processes text to:
  - Remove **stop words**, numbers, and empty content.
- Calculates **word frequencies** and identifies:  
   - **Most frequent words**.  
   - **Least frequent words**.  
- Utilizes **Apache Spark** for parallel data processing.

---

### ✅ **Problem 3: Sentiment Analysis Using Bag-of-Words (BOW)**
- Connects to **MongoDB** to fetch article titles.
- Uses **positive** and **negative word lists** to:
  - Compute word **scores**.  
  - Determine the **polarity** of each title:  
    - **Positive** 😊  
    - **Negative** 😞  
    - **Neutral** 😐  
- Stores the sentiment analysis results in **results.csv**.  

---

## ⚙️ **Execution Instructions**

### 🚨 **Prerequisites**
Before running the program, ensure you have:
1. **MongoDB Atlas** account and a cluster setup.
2. **Apache Spark** and **Java** installed.
3. Required input files:  
   - Reuters SGM files: e.g., `reut2-014.sgm`  
   - Positive words: `positive-words.txt`  
   - Negative words: `negative-words.txt`

---

### 🛠️ **Steps to Execute**

#### 🔹 **Problem 1A: MongoDB Storage**
1. **Setup MongoDB Atlas** and create a cluster.  
2. Update MongoDB connection details in the Java program.  
3. Run the program to insert cleaned articles into MongoDB.

#### 🔹 **Problem 1B: Word Frequency Analysis**
1. Start a **Spark Cluster** (locally or on Google Dataproc).  
2. Upload:  
   - **JAR file**  
   - Reuters input file: `reut2-014.sgm`.  
3. Run the program to compute **word frequencies**.

#### 🔹 **Problem 3: Sentiment Analysis**
1. Update **MongoDB connection details**.  
2. Provide paths to `positive-words.txt` and `negative-words.txt`.  
3. Run the program. Results will be saved in **results.csv**.

---

## 📊 **Outputs**

1.  **Problem 1A**: Articles stored in MongoDB (`ReuterDb > articles`).  
2.  **Problem 1B**: Word frequencies calculated and displayed.  
3.  **Problem 3**: Sentiment analysis results stored in **results.csv**.



