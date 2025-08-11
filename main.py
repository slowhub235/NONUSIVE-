
import discord
from discord import app_commands
import random
import string
import json
import os
from datetime import datetime, timedelta
from flask import Flask
from threading import Thread

# =============== KEEP ALIVE (UptimeRobot) ===============
app = Flask('')

@app.route('/')
def home():
    return "Xynware Bot is alive!"

def run():
    app.run(host='0.0.0.0', port=8080)

def keep_alive():
    t = Thread(target=run)
    t.daemon = True
    t.start()

# ========================================================

TOKEN = os.getenv("TOKEN")  # Put your bot token in Replit Secrets
KEYS_FILE = "keys.json"
USERS_FILE = "users.json"
OWNER_ID = '1379236573400404069'  # Replace with your Discord user ID

# Ensure keys.json exists
if not os.path.exists(KEYS_FILE):
    with open(KEYS_FILE, "w") as f:
        json.dump({}, f)

# Ensure users.json exists
if not os.path.exists(USERS_FILE):
    with open(USERS_FILE, "w") as f:
        json.dump({}, f)

def load_keys():
    with open(KEYS_FILE, "r") as f:
        return json.load(f)

def save_keys(data):
    with open(KEYS_FILE, "w") as f:
        json.dump(data, f, indent=4)

def load_users():
    with open(USERS_FILE, "r") as f:
        return json.load(f)

def save_users(data):
    with open(USERS_FILE, "w") as f:
        json.dump(data, f, indent=4)

def generate_key(length=18):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

TASKS = [
    ("Type the number **12345**", "12345"),
    ("Say the word **banana**", "banana"),
    ("Type today's year", "2025"),
    ("Type the color **red**", "red"),
    ("Say the first letter of the alphabet", "a"),
    ("Type the word **xynware**", "xynware"),
]

class XynwareView(discord.ui.View):
    def __init__(self):
        super().__init__(timeout=None)  # Persistent view

    @discord.ui.button(label="Redeem Key", style=discord.ButtonStyle.green, custom_id="redeem_key")
    async def redeem(self, interaction: discord.Interaction, button: discord.ui.Button):
        await interaction.response.send_message("Check your DMs to redeem your key.", ephemeral=True)
        try:
            await interaction.user.send("Please enter your key:")
        except:
            return

        def check(msg):
            return msg.author == interaction.user and isinstance(msg.channel, discord.DMChannel)

        msg = await bot.wait_for("message", check=check)
        keys = load_keys()
        if msg.content in keys and not keys[msg.content]["redeemed"]:
            keys[msg.content]["redeemed"] = True
            save_keys(keys)
            users = load_users()
            user_id = str(interaction.user.id)
            expiry = None if keys[msg.content].get("permanent") else datetime.now() + timedelta(hours=1)
            users[user_id] = {"expiry": expiry.isoformat() if expiry else None}
            save_users(users)
            await interaction.user.send("✅ Key redeemed! Your access is " + ("permanent." if expiry is None else "valid for 1 hour.") + " Your loadstring is:\n```lua\nloadstring(game:HttpGet(\"https://raw.githubusercontent.com/slowhub235/xynnware.balls/refs/heads/main/balls/balls/balls/ball/ball/ball/.lua\"))()\n```")
        elif msg.content in keys and keys[msg.content]["redeemed"]:
            await interaction.user.send("⚠️ This key has already been redeemed.")
        else:
            await interaction.user.send("❌ Invalid key.")

    @discord.ui.button(label="Get Script", style=discord.ButtonStyle.blurple, custom_id="get_script")
    async def get_script(self, interaction: discord.Interaction, button: discord.ui.Button):
        user_id = str(interaction.user.id)
        users = load_users()
        if user_id in users:
            expiry = users[user_id]["expiry"]
            if expiry is None or datetime.fromisoformat(expiry) > datetime.now():
                await interaction.response.send_message("Your loadstring is:\n```lua\nloadstring(game:HttpGet(\"https://raw.githubusercontent.com/slowhub235/xynnware.balls/refs/heads/main/balls/balls/balls/ball/ball/ball/.lua\"))()\n```", ephemeral=True)
            else:
                await interaction.response.send_message("Your key has expired. Please redeem a new one.", ephemeral=True)
        else:
            await interaction.response.send_message("You need to redeem a key first.", ephemeral=True)

    @discord.ui.button(label="Reset HWID", style=discord.ButtonStyle.grey, custom_id="reset_hwid")
    async def reset_hwid(self, interaction: discord.Interaction, button: discord.ui.Button):
        user_id = str(interaction.user.id)
        users = load_users()
        if user_id in users:
            expiry = users[user_id]["expiry"]
            if expiry is None or datetime.fromisoformat(expiry) > datetime.now():
                await interaction.response.send_message("HWID has been reset!", ephemeral=True)
                # Add any additional HWID reset logic here if needed
            else:
                await interaction.response.send_message("Your key has expired. Please redeem a new one.", ephemeral=True)
        else:
            await interaction.response.send_message("You need to redeem a key first.", ephemeral=True)

class BotClient(discord.Client):
    def __init__(self):
        intents = discord.Intents.default()
        intents.message_content = True
        super().__init__(intents=intents)
        self.tree = app_commands.CommandTree(self)

    async def setup_hook(self):
        # Re-register the persistent view so buttons work after restart
        self.add_view(XynwareView())
        await self.tree.sync()
        print("✅ Slash commands synced.")

bot = BotClient()

@bot.tree.command(name="getkey", description="Complete 3 tasks to get a key")
async def getkey(interaction: discord.Interaction):
    await interaction.response.send_message("Check your DMs to complete the tasks.", ephemeral=True)
    try:
        await interaction.user.send("Let's complete 3 quick tasks!")
    except:
        return

    selected_tasks = random.sample(TASKS, 3)
    for question, answer in selected_tasks:
        await interaction.user.send(question)
        def check(msg):
            return msg.author == interaction.user and isinstance(msg.channel, discord.DMChannel)
        msg = await bot.wait_for("message", check=check)
        if msg.content.lower() != answer.lower():
            await interaction.user.send("❌ Wrong answer! Task failed.")
            return

    key = generate_key()
    keys = load_keys()
    keys[key] = {"redeemed": False}
    save_keys(keys)
    await interaction.user.send(f"✅ All tasks complete! Your key is:\n```\n{key}\n```")

@bot.tree.command(name="pnl", description="Show the main panel")
async def pnl(interaction: discord.Interaction):
    embed = discord.Embed(
        title="Xynware Main Control Panel",
        color=0x2f3136  # Dark color to match the image theme
    )
    embed.add_field(
        name="Users (1 Hour Access)",
        value='• If you have redeemed a key, click "Get Script" to get the loader.',
        inline=False
    )
    embed.add_field(
        name="Mobile Users:",
        value='When copying the script, remember to delete the lua and " part. You must include script_key or the script wont work.',
        inline=False
    )
    embed.add_field(
        name="Free Users",
        value='• There is no "Free" version of Xynware. Complete tasks with /getkey to obtain a key.',
        inline=False
    )
    embed.set_footer(text=f"Sent by {interaction.user.name} • {datetime.now().strftime('%d/%m/%Y %I:%M %p')}")
    await interaction.response.send_message(embed=embed, view=XynwareView())

@bot.tree.command(name="permkey", description="Generate a permanent key for a user (owner only)")
@app_commands.describe(user="The user to receive the permanent key")
async def permkey(interaction: discord.Interaction, user: discord.User):
    if str(interaction.user.id) != OWNER_ID:
        await interaction.response.send_message("❌ You are not authorized to use this command.", ephemeral=True)
        return

    key = generate_key()
    keys = load_keys()
    keys[key] = {"redeemed": False, "permanent": True}
    save_keys(keys)
    try:
        await user.send(f"🎉 You have received a permanent key for Xynware!\nYour key is:\n```\n{key}\n```\nUse it in the panel to redeem your permanent access.")
        await interaction.response.send_message(f"✅ Permanent key sent to {user.name}.", ephemeral=True)
    except:
        await interaction.response.send_message(f"❌ Could not DM {user.name}. Their DMs may be closed.", ephemeral=True)

@bot.event
async def on_ready():
    print(f"✅ Bot logged in as {bot.user}")

if TOKEN is None:
    raise ValueError("TOKEN environment variable not set! Please add it in Replit Secrets.")

keep_alive()  # Start the web server for UptimeRobot
bot.run(TOKEN)
