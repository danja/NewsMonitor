import os
import re
import sys
import requests
from bs4 import BeautifulSoup
from markdown import markdown
from urllib.parse import urljoin
import validators

# Helper function to save list to file
def save_list_to_file(list, filename):
    try:
        with open(filename, 'w') as f:
            for item in list:
                f.write("%s\n" % item)
    except Exception as e:
        print(f"Error writing to file {filename}: {e}")

# Regex-based URL extractor
def extract_urls_with_regex(content):
    url_regex = re.compile(
        r'http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\\(\\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+'
    )
    urls = re.findall(url_regex, content)
    return urls

# Helper function to parse and extract URLs
def extract_urls(content, base_url):
    try:
        soup = BeautifulSoup(content, 'html.parser')
        urls = [urljoin(base_url, link.get('href')) for link in soup.find_all('a')]
        return urls
    except Exception:
        print("Error parsing URLs with BeautifulSoup, trying regex instead.")
        return extract_urls_with_regex(content)

# Helper function to parse Markdown and convert to HTML
def convert_md_to_html(md_content):
    html = markdown(md_content)
    return html

# Function to check if URL is a feed
def is_feed(url):
    try:
        response = requests.get(url)
        content_type = response.headers.get('Content-Type')
        return 'application/rss+xml' in content_type or 'application/atom+xml' in content_type
    except Exception:
        print(f"Error checking if URL is a feed: {url}")
        return False

# Check if URL or file path is provided as a command-line argument
if len(sys.argv) < 2:
    print("Please provide a URL or file path as a command-line argument.")
    sys.exit(1)

# Initial URL or file path
url_or_file = sys.argv[1]

# Check if it's a valid URL
if validators.url(url_or_file):
    # It's a URL, send a HTTP GET request
    try:
        response = requests.get(url_or_file)
        content = response.text
        base_url = url_or_file
    except Exception:
        print(f"Error sending a HTTP GET request to {url_or_file}")
        sys.exit(1)
else:
    # It's not a URL, try opening it as a file
    try:
        with open(url_or_file, 'r') as f:
            content = f.read()
        base_url = ''
    except Exception:
        print(f"Error opening {url_or_file} as a file")
        sys.exit(1)

# Save the content to a local file
save_list_to_file(content, 'page.html')

# Extract URLs from the content
urls = extract_urls(content, base_url)

# Save these URLs to a local file
save_list_to_file(urls, 'urls.txt')

# For each URL, send a HTTP GET request, extract links to Atom/RSS feeds, and save these links to a local file
feed_urls = []
for url in urls:
    print(f"Processing URL: {url}")
    try:
        response = requests.get(url)
        content_type = response.headers.get('Content-Type')

        if 'text/html' in content_type:
            content = response.text
        elif 'text/markdown' in content_type:
            content = convert_md_to_html(response.text)
        elif 'text/plain' in content_type:
            content = '<p>' + response.text + '</p>'
        else:
            continue

        soup = BeautifulSoup(content, 'html.parser')
        feed_links = soup.select('link[type="application/rss+xml"], link[type="application/atom+xml"]')

        for link in feed_links:
            feed_url = urljoin(url, link.get('href'))
            if feed_url not in feed_urls:
                feed_urls.append(feed_url)
    except Exception:
        print(f"Error processing URL {url}")

# Save feed URLs to a local file
save_list_to_file(feed_urls, 'feed_urls.txt')

# Check each feed URL and save valid feeds to another file
checked_feed_urls = [url for url in feed_urls if is_feed(url)]
save_list_to_file(checked_feed_urls, 'checked_feed_urls.txt')
