import os
import shutil
from PIL import Image

src_boards = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoOfficialAssets\boards"
src_ui = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoOfficialAssets\ui"
src_dice = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoApp\preview\official_assets\dice"

dest_web = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoApp\preview\official_assets"
dest_android = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoApp\app\src\main\res\drawable"
os.makedirs(dest_web, exist_ok=True)
os.makedirs(dest_android, exist_ok=True)

# 1. Official Board (512x512 with Red, Green, Blue, Yellow yards)
board_src = os.path.join(src_boards, "tex_642_512x512.png")
if not os.path.exists(board_src):
    board_src = os.path.join(src_boards, "tex_76_512x512.png")

shutil.copy(board_src, os.path.join(dest_web, "official_board.png"))
shutil.copy(board_src, os.path.join(dest_android, "official_board.png"))
print("Board copied successfully!")

# 2. Official Pawns
pawns = {
    "pawn_red.png": os.path.join(src_ui, "asset_12_54x76.png"),
    "pawn_green.png": os.path.join(src_ui, "asset_17_53x73.png"),
    "pawn_blue.png": os.path.join(src_ui, "asset_27_54x76.png"),
    "pawn_yellow.png": os.path.join(src_ui, "asset_10_64x80.png")
}

for name, src in pawns.items():
    if os.path.exists(src):
        shutil.copy(src, os.path.join(dest_web, name))
        shutil.copy(src, os.path.join(dest_android, name))
        print(f"Pawn {name} copied!")

# 3. Official Dice Faces (Extract the clearest landing face from each dice pack)
dice_dir = dest_dice = os.path.join(dest_web, "dice")
for val in range(1, 7):
    # Find matching files in dice folder
    matching = [f for f in os.listdir(dice_dir) if f.startswith(f"dice_{val}_frame_")]
    if matching:
        # Choose the first large clear frame
        chosen = matching[0]
        src_f = os.path.join(dice_dir, chosen)
        dest_name = f"dice_{val}.png"
        shutil.copy(src_f, os.path.join(dest_web, dest_name))
        shutil.copy(src_f, os.path.join(dest_android, dest_name))
        print(f"Dice face {val} copied ({chosen})!")

# 4. Official App Logo & Crown
logo_src = r"C:\Users\Bhomic\.gemini\antigravity\brain\cff53fb7-89b7-4321-aaed-0a0ed9d96105\.user_uploaded\media_1793709853904_e6de1115.png"
if os.path.exists(logo_src):
    shutil.copy(logo_src, os.path.join(dest_web, "official_logo.png"))
    shutil.copy(logo_src, os.path.join(dest_android, "official_logo.png"))

print("All official assets assembled!")

