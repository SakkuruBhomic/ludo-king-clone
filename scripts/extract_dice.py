import UnityPy, zipfile, os

apk_path = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoKing_Official_Extracted\UnityDataAssetPack.apk"
out_dir = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoApp\preview\official_assets\dice"
os.makedirs(out_dir, exist_ok=True)

with zipfile.ZipFile(apk_path, "r") as z:
    for val in range(6):
        pack_name = f"assets/SNL/Android/DICE/dicepack1_{val}"
        if pack_name in z.namelist():
            data = z.read(pack_name)
            env = UnityPy.load(data)
            frame_idx = 0
            for obj in env.objects:
                if obj.type.name in ["Texture2D", "Sprite"]:
                    try:
                        d = obj.read()
                        img = d.image
                        w, h = img.size
                        if w >= 64 and h >= 64:
                            out_file = os.path.join(out_dir, f"dice_{val+1}_frame_{frame_idx}_{w}x{h}.png")
                            img.save(out_file)
                            frame_idx += 1
                    except Exception as e:
                        pass
            print(f"Extracted {frame_idx} frames for dice value {val+1}")

