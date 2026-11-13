import zipfile, re

apk_path = r"C:\Users\Bhomic\.gemini\antigravity\scratch\LudoKing_Official_Extracted\com.ludo.king.apk"

with zipfile.ZipFile(apk_path, "r") as z:
    meta_bytes = z.read("assets/bin/Data/Managed/Metadata/global-metadata.dat")
    print(f"global-metadata.dat size: {len(meta_bytes)} bytes")
    
    # Search for string literals in metadata
    text = meta_bytes.decode("latin1", errors="ignore")
    
    # Search for PassNPlay or DiceManager classes/methods
    matches = set(re.findall(r'[A-Za-z0-9_]*(?:PassNPlay|PassAndPlay|DiceManager|LudoGame|DiceRoll|RollDice)[A-Za-z0-9_]*', text))
    print(f"Found {len(matches)} relevant identifiers:")
    for m in sorted(matches)[:40]:
        print("  ", m)

