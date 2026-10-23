
import pymupdf
import requests

# Step 1: Open the PDF
pdf_path = "uploads/CLOUD DATA ANALYTICS (2).PDF"
document = pymupdf.open(pdf_path)

# Step 2: Extract text from all pages
full_text = ""

for page in document:
    full_text += page.get_text() + "\n"

document.close()

print("PDF text extracted successfully!")
print("Total characters:", len(full_text))

# Step 3: Ask Ollama to summarize the PDF
url = "http://localhost:11434/api/generate"

prompt = f"""
Summarize the following document in simple English.
Give the main points as bullet points.
Use only information from the document.

Document text:
{full_text}
"""

response = requests.post(
    url,
    json={
        "model": "qwen2.5:1.5b",
        "prompt": prompt,
        "stream": False
    },
    timeout=180
)

response.raise_for_status()
summary = response.json()["response"]

# Step 4: Display the summary
print("\n===== AI SUMMARY =====\n")
print(summary)

with open("ai_summary.txt", "w", encoding="utf-8") as file:
    file.write(summary)

print("\nAI summary saved to ai_summary.txt")
