import requests
import mysql.connector
import json
import os
# ----------------------------
# Groq API key (get free at https://console.groq.com)
# ----------------------------
GROQ_API_KEY = "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"

# ----------------------------
# Fetch hospital + doctor data in JSON format
# ----------------------------
prompt = """
Give me 3 hospitals in Sri Lanka in JSON format.
Each hospital should have: name, type, rating, open_hours, map, telephone.
Also include 2 doctors per hospital with: name, specialty, experience.
Format as a JSON object: 
{
  "hospitals": [
    {
      "name": "...",
      "type": "...",
      "rating": ...,
      "open_hours": "...",
      "map": "...",
      "telephone": "...",
      "doctors": [
        {"name": "...", "specialty": "...", "experience": ...},
        ...
      ]
    }
  ]
}
Return ONLY the JSON, no explanation.
"""

# Call Groq API
response = requests.post(
    "https://api.groq.com/openai/v1/chat/completions",
    headers={
        "Authorization": f"Bearer {GROQ_API_KEY}",
        "Content-Type": "application/json"
    },
    json={
        "model": "llama-3.3-70b-versatile",
        "messages": [{"role": "user", "content": prompt}],
        "temperature": 0.7
    }
)

# Extract content
result = response.json()

# Debug: Print the API response to see errors
if 'error' in result or 'choices' not in result:
    print("API Error Response:")
    print(json.dumps(result, indent=2))
    print("\nMake sure your GROQ_API_KEY is set correctly!")
    exit(1)

data = result['choices'][0]['message']['content']

# Clean up and parse JSON
data = data.strip()
# Remove markdown code blocks if present
if data.startswith("```"):
    data = data.split("```")[1]
    if data.startswith("json"):
        data = data[4:]
data = data.strip()
hospital_data = json.loads(data)

print("Fetched data from Groq API:")
print(json.dumps(hospital_data, indent=2))

# ----------------------------
# Connect to MySQL
# ----------------------------
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="root",
    database="mediseek"
)
cursor = db.cursor()

# ----------------------------
# Insert hospitals, doctors, and doctor_hospital
# ----------------------------
for hospital in hospital_data['hospitals']:
    # Insert hospital
    cursor.execute("""
        INSERT INTO hospital (name, type, rating, open_hours, map, telephone)
        VALUES (%s, %s, %s, %s, %s, %s)
    """, (
        hospital['name'],
        hospital['type'],
        hospital.get('rating', None),
        hospital['open_hours'],
        hospital['map'],
        hospital['telephone']
    ))
    hospital_id = cursor.lastrowid

    # Insert doctors for this hospital
    for doctor in hospital['doctors']:
        cursor.execute("""
            INSERT INTO doctor (name, specialty, experience)
            VALUES (%s, %s, %s)
        """, (
            doctor['name'],
            doctor['specialty'],
            doctor['experience']
        ))
        doctor_id = cursor.lastrowid

        # Insert into doctor_hospital join table
        cursor.execute("""
            INSERT INTO doctor_hospital (doctor_id, hospital_id)
            VALUES (%s, %s)
        """, (doctor_id, hospital_id))

# Commit and close
db.commit()
cursor.close()
db.close()

print("Hospitals, doctors, and relationships inserted successfully!")
