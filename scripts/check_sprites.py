import UnityPy, zipfile

apk_path = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoKing_Official_Extracted\UnityDataAssetPack.apk"
with zipfile.ZipFile(apk_path, "r") as z:
    for base in ["sharedassets2.assets", "sharedassets21.assets"]:
        splits = sorted([n for n in z.namelist() if base in n and ".split" in n], key=lambda x: int(x.split(".split")[1]))
        combined = b"".join(z.read(n) for n in splits)
        env = UnityPy.load(combined)
        sprite_names = []
        for o in env.objects:
            if o.type.name == "Sprite":
                try:
                    s = o.read()
                    if s.name:
                        sprite_names.append(s.name)
                except Exception:
                    pass
        print(f"{base}: Found {len(sprite_names)} named sprites!")
        print("Sample sprite names:", sprite_names[:30])

