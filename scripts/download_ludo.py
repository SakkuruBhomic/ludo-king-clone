import urllib.request
import sys
import os
import time

url = 'https://d.apkpure.net/b/APK/com.ludo.king?version=latest'
out_path = r'C:\Users\Bhomic\.gemini\antigravity\scratch\LudoKing_Official.xapk'
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'
}

print(f"Connecting to {url}...")
req = urllib.request.Request(url, headers=headers)

start_time = time.time()
with urllib.request.urlopen(req) as resp, open(out_path, 'wb') as out_file:
    total_size = int(resp.headers.get('Content-Length', 0))
    print(f"Total size: {total_size / (1024*1024):.2f} MB")
    
    downloaded = 0
    block_size = 1024 * 1024 # 1 MB
    
    while True:
        buffer = resp.read(block_size)
        if not buffer:
            break
        downloaded += len(buffer)
        out_file.write(buffer)
        percent = (downloaded / total_size) * 100 if total_size else 0
        sys.stdout.write(f"\rDownloading: {downloaded / (1024*1024):.1f} / {total_size / (1024*1024):.1f} MB ({percent:.1f}%)")
        sys.stdout.flush()

duration = time.time() - start_time
print(f"\nDownload completed in {duration:.1f}s! Saved to: {out_path}")

