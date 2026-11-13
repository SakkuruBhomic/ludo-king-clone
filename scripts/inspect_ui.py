import os
from PIL import Image

ui_dir = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoOfficialAssets\ui"
files = os.listdir(ui_dir)
print(f"Total files in UI: {len(files)}")

# Let's inspect some of the named files vs unnamed
named = [f for f in files if not f.startswith("tex_") and not f.startswith("asset_")]
print(f"Named assets: {len(named)}")
for n in named[:40]:
    print("  ", n)

