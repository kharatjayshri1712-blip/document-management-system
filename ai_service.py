
from flask import Flask, request, jsonify
import requests
import pymupdf
import os

app = Flask(__name__)

OLLAMA_URL = "http://localhost:11434/api/generate"
MODEL_NAME = "qwen2.5:1.5b"

UPLOAD_DIR = os.path.join(os.getcwd(), "uploads")


def generate_summary(text):
    prompt = f"""
    Summarize the following document in simple English.
    Provide the main points as bullet points.
    Use only information from the document.

    Document:
    {text}
    """

    response = requests.post(
        OLLAMA_URL,
        json={
            "model": MODEL_NAME,
            "prompt": prompt,
            "stream": False
        },
        timeout=180
    )

    response.raise_for_status()
    return response.json()["response"]


@app.route("/summarize", methods=["POST"])
def summarize():
    data = request.get_json(silent=True) or {}
    text = data.get("text", "").strip()

    if not text:
        return jsonify({"error": "Document text is required"}), 400

    try:
        summary = generate_summary(text)
        return jsonify({"summary": summary})

    except requests.RequestException:
        return jsonify({"error": "AI model is unavailable"}), 502


@app.route("/summarize-pdf", methods=["POST"])
def summarize_pdf():
    data = request.get_json(silent=True) or {}
    file_name = data.get("fileName", "").strip()

    if not file_name:
        return jsonify({"error": "PDF file name is required"}), 400

    # Accept only a filename, not a path to another directory.
    if os.path.basename(file_name) != file_name:
        return jsonify({"error": "Invalid file name"}), 400

    if not file_name.lower().endswith(".pdf"):
        return jsonify({"error": "Only PDF files are supported"}), 400

    file_path = os.path.join(UPLOAD_DIR, file_name)

    if not os.path.isfile(file_path):
        return jsonify({"error": "PDF file not found"}), 404

    try:
        with pymupdf.open(file_path) as document:
            text = "\n".join(page.get_text() for page in document)

        if not text.strip():
            return jsonify({"error": "No text found in this PDF"}), 422

        summary = generate_summary(text)
        return jsonify({
            "fileName": file_name,
            "summary": summary
        })

    except requests.RequestException:
        return jsonify({"error": "AI model is unavailable"}), 502

    except Exception:
        app.logger.exception("Failed to process PDF: %s", file_name)
        return jsonify({"error": "Could not process this PDF"}), 500


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=False)
