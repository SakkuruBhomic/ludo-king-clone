import os
import zipfile
import UnityPy
from PIL import Image

output_dir = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoOfficialAssets"
os.makedirs(output_dir, exist_ok=True)
os.makedirs(os.path.join(output_dir, "images"), exist_ok=True)
os.makedirs(os.path.join(output_dir, "audio"), exist_ok=True)
os.makedirs(os.path.join(output_dir, "boards"), exist_ok=True)
os.makedirs(os.path.join(output_dir, "ui"), exist_ok=True)
os.makedirs(os.path.join(output_dir, "dice"), exist_ok=True)

apk_path = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoKing_Official_Extracted\UnityDataAssetPack.apk"

print(f"Scanning asset packs from {apk_path}...")

with zipfile.ZipFile(apk_path, "r") as z:
    # 1. First process non-split asset files
    for name in z.namelist():
        if name.endswith(".assets") and ".split" not in name:
            try:
                data = z.read(name)
                env = UnityPy.load(data)
                for obj in env.objects:
                    if obj.type.name in ["Texture2D", "Sprite"]:
                        data_obj = obj.read()
                        try:
                            img = data_obj.image
                            img_name = getattr(data_obj, "name", "") or f"asset_{obj.path_id}"
                            w, h = img.size
                            if w >= 512 and h >= 512:
                                out = os.path.join(output_dir, "boards", f"{img_name}_{w}x{h}.png")
                            elif w >= 64 or h >= 64:
                                out = os.path.join(output_dir, "ui", f"{img_name}_{w}x{h}.png")
                            else:
                                out = os.path.join(output_dir, "images", f"{img_name}_{w}x{h}.png")
                            img.save(out)
                        except Exception:
                            pass
                    elif obj.type.name == "AudioClip":
                        clip = obj.read()
                        try:
                            for sample_name, sample_data in clip.samples.items():
                                out = os.path.join(output_dir, "audio", f"{sample_name}.wav")
                                with open(out, "wb") as f:
                                    f.write(sample_data)
                        except Exception:
                            pass
            except Exception as e:
                print(f"Error on {name}: {e}")

    # 2. Process split asset files (e.g. sharedassets2, sharedassets21)
    for base in ["sharedassets2.assets", "sharedassets21.assets", "sharedassets8.assets"]:
        splits = sorted([n for n in z.namelist() if base in n and ".split" in n], key=lambda x: int(x.split(".split")[1]))
        if splits:
            print(f"Loading {base} ({len(splits)} parts)...")
            combined = b"".join(z.read(n) for n in splits)
            env = UnityPy.load(combined)
            print(f"Extracting textures from {base}...")
            count = 0
            for obj in env.objects:
                if obj.type.name in ["Texture2D", "Sprite"]:
                    try:
                        data_obj = obj.read()
                        img = data_obj.image
                        img_name = getattr(data_obj, "name", "") or f"tex_{obj.path_id}"
                        w, h = img.size
                        # Filter out tiny 1x1 or 2x2 placeholders
                        if w >= 32 and h >= 32:
                            if w >= 500 and h >= 500:
                                target_sub = "boards"
                            elif "dice" in img_name.lower():
                                target_sub = "dice"
                            else:
                                target_sub = "ui"
                            out = os.path.join(output_dir, target_sub, f"{img_name}_{w}x{h}.png")
                            img.save(out)
                            count += 1
                    except Exception:
                        pass
                elif obj.type.name == "AudioClip":
                    try:
                        clip = obj.read()
                        for sample_name, sample_data in clip.samples.items():
                            out = os.path.join(output_dir, "audio", f"{sample_name}.wav")
                            with open(out, "wb") as f:
                                f.write(sample_data)
                    except Exception:
                        pass
            print(f"Extracted {count} images from {base}")

print("Asset extraction completed successfully!")

