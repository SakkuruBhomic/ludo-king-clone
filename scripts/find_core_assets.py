import os
from PIL import Image

output_dir = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoOfficialAssets"
boards_dir = os.path.join(output_dir, "boards")
ui_dir = os.path.join(output_dir, "ui")

# Let's inspect board textures to find the classic Ludo King board
print("Searching for the exact classic Ludo King board...")
for f in os.listdir(boards_dir):
    fp = os.path.join(boards_dir, f)
    try:
        with Image.open(fp) as img:
            w, h = img.size
            if w == h and w in [1024, 1200, 2048]:
                # Check center pixel and corners
                center = img.getpixel((w//2, h//2))[:3]
                print(f"Board candidate: {f} ({w}x{h}), center={center}")
    except: pass

# Let's search UI folder for pawn/token shapes (transparent background, tall aspect ratio, round head)
print("\nSearching for pawns/tokens in UI...")
pawns = []
for f in os.listdir(ui_dir):
    fp = os.path.join(ui_dir, f)
    try:
        with Image.open(fp) as img:
            w, h = img.size
            # Pawn is typically aspect ratio 1:1.5 to 1:2.5, width between 40 and 200
            if 0.4 <= (w / h) <= 0.8 and 40 <= w <= 250:
                # Check if it has transparency (RGBA)
                if img.mode == 'RGBA':
                    # Sample non-transparent colors
                    pixels = list(img.getdata())
                    non_alpha = [p for p in pixels if p[3] > 200]
                    if len(non_alpha) > 100:
                        pawns.append((f, w, h, len(non_alpha)))
    except: pass

print(f"Found {len(pawns)} pawn-like candidate sprites:")
for p in pawns[:15]:
    print("  ", p)

