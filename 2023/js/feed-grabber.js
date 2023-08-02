const axios = require('axios');
const cheerio = require('cheerio');
const MarkdownIt = require('markdown-it');
const fs = require('fs');
const validUrl = require('valid-url');

// Helper function to save list to file
function saveListToFile(list, filename) {
    fs.writeFileSync(filename, list.join('\n'));
}

// Helper function to parse and extract URLs
function extractUrls(html, base_url) {
    const $ = cheerio.load(html);
    let urls = [];
    $('a').each((index, element) => {
        const url = new URL($(element).attr('href'), base_url);
        urls.push(url.toString());
    });
    return urls;
}

// Function to check if URL is a feed
async function isFeed(url) {
    try {
        const response = await axios.head(url);
        const contentType = response.headers['content-type'];
        return contentType.includes('application/rss+xml') || contentType.includes('application/atom+xml');
    } catch (error) {
        console.log(`Error checking if URL is a feed: ${url}`);
        return false;
    }
}

// Initial URL or file path
let urlOrFile = process.argv[2];
let content, base_url;

if (validUrl.isWebUri(urlOrFile)) {
    // It's a URL, send a HTTP GET request
    axios.get(urlOrFile)
        .then(response => {
            content = response.data;
            base_url = urlOrFile;
            main();
        })
        .catch(error => console.log(`Error sending a HTTP GET request to ${urlOrFile}`));
} else {
    // It's not a URL, try opening it as a file
    try {
        content = fs.readFileSync(urlOrFile, 'utf-8');
        base_url = '';
        main();
    } catch (error) {
        console.log(`Error opening ${urlOrFile} as a file`);
    }
}

async function main() {
    // Save the content to a local file
    fs.writeFileSync('page.html', content);

    // Extract URLs from the content
    let urls = extractUrls(content, base_url);

    // Save these URLs to a local file
    saveListToFile(urls, 'urls.txt');

    // For each URL, send a HTTP GET request, extract links to Atom/RSS feeds, and save these links to a local file
    let feedUrls = [];
    for (let url of urls) {
        console.log(`Processing URL: ${url}`);
        try {
            const response = await axios.get(url);
            const contentType = response.headers['content-type'];
            let content;

            if (contentType.includes('text/html')) {
                content = response.data;
            } else if (contentType.includes('text/markdown')) {
                const md = new MarkdownIt();
                content = md.render(response.data);
            } else if (contentType.includes('text/plain')) {
                content = `<p>${response.data}</p>`;
            } else {
                continue;
            }

            const $ = cheerio.load(content);
            $('link[type="application/rss+xml"], link[type="application/atom+xml"]').each((index, element) => {
                const feedUrl = new URL($(element).attr('href'), url);
                if (!feedUrls.includes(feedUrl.toString())) {
                    feedUrls.push(feedUrl.toString());
                }
            });
        } catch (error) {
            console.log(`Error processing URL ${url}`);
        }
    }

    // Save feed URLs to a local file
    saveListToFile(feedUrls, 'feed_urls.txt');

    // Check each feed URL and save valid feeds to another file
    let checkedFeedUrls = [];
    for (let url of feedUrls) {
        if (await isFeed(url)) {
            checkedFeedUrls.push(url);
        }
    }
    saveListToFile(checkedFeedUrls, 'checked_feed_urls.txt');
}
