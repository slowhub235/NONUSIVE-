--// Services
local Players = game:GetService("Players")
local CoreGui = game:GetService("CoreGui")
local TweenService = game:GetService("TweenService")
local UserInputService = game:GetService("UserInputService")
local RunService = game:GetService("RunService")
local LocalPlayer = Players.LocalPlayer
local Workspace = game:GetService("Workspace")

--// Main UI
local gui = Instance.new("ScreenGui", CoreGui)
gui.Name = "BladeBallUI"
gui.ResetOnSpawn = false

local main = Instance.new("Frame", gui)
main.Size = UDim2.new(0, 700, 0, 400)
main.Position = UDim2.new(0.5, -350, 0.5, -200)
main.BackgroundColor3 = Color3.fromRGB(20, 20, 20)
main.Active = true
main.Draggable = true
main.BorderSizePixel = 0
Instance.new("UICorner", main).CornerRadius = UDim.new(0, 24)

--// Top Bar
local topBar = Instance.new("Frame", main)
topBar.Size = UDim2.new(1, 0, 0, 35)
topBar.Position = UDim2.new(0, 0, 0, 0)
topBar.BackgroundColor3 = Color3.fromRGB(15, 15, 15)
topBar.BorderSizePixel = 0
Instance.new("UICorner", topBar).CornerRadius = UDim.new(0, 16)

local title = Instance.new("TextLabel", topBar)
title.Text = "VERTO.BB⚔️"
title.Size = UDim2.new(1, 0, 1, 0)
title.TextColor3 = Color3.fromRGB(200, 200, 200)
title.Font = Enum.Font.GothamBold
title.TextSize = 16
title.BackgroundTransparency = 1
title.TextXAlignment = Enum.TextXAlignment.Left
title.Position = UDim2.new(0, 12, 0, 0)

--// Sidebar
local sidebar = Instance.new("Frame", main)
sidebar.Position = UDim2.new(0, 0, 0, 35)
sidebar.Size = UDim2.new(0, 120, 1, -35)
sidebar.BackgroundColor3 = Color3.fromRGB(15, 15, 15)
sidebar.BorderSizePixel = 0
Instance.new("UICorner", sidebar).CornerRadius = UDim.new(0, 20)

--// Tab Handling
local tabs = {}
local content = Instance.new("Frame", main)
content.Position = UDim2.new(0, 130, 0, 45)
content.Size = UDim2.new(1, -140, 1, -55)
content.BackgroundTransparency = 1

local function switchTab(name)
    for tabName, frame in pairs(tabs) do
        frame.Visible = (tabName == name)
    end
    for _, tabBtn in ipairs(sidebar:GetChildren()) do
        if tabBtn:IsA("TextButton") then
            local isActive = tabBtn.Name == name
            tabBtn.BackgroundColor3 = isActive and Color3.fromRGB(35, 60, 150) or Color3.fromRGB(25, 25, 25)
        end
    end
end

-- Sidebar Tabs
local tabNames = {"Home", "Main", "ESP", "Shop", "Settings"}
local tabIcons = {
    Home = "📢",
    Main = "📋",
    ESP = "👁️",
    Shop = "🛒",
    Settings = "⚙️"
}
local activeTab = "Main"

for i, name in ipairs(tabNames) do
    local tab = Instance.new("TextButton", sidebar)
    tab.Name = name
    tab.Size = UDim2.new(1, -20, 0, 40)
    tab.Position = UDim2.new(0, 10, 0, 10 + (i - 1) * 50)
    tab.BackgroundColor3 = name == activeTab and Color3.fromRGB(35, 60, 150) or Color3.fromRGB(25, 25, 25)
    tab.Text = "   " .. tabIcons[name] .. "  " .. name
    tab.TextColor3 = Color3.fromRGB(180, 180, 180)
    tab.TextSize = 14
    tab.Font = Enum.Font.Gotham
    tab.TextXAlignment = Enum.TextXAlignment.Left
    tab.BorderSizePixel = 0
    Instance.new("UICorner", tab).CornerRadius = UDim.new(0, 10)

    local page = Instance.new("Frame", content)
    page.Name = name
    page.Size = UDim2.new(1, 0, 1, 0)
    page.BackgroundTransparency = 1
    page.Visible = (name == activeTab)
    tabs[name] = page

    tab.MouseButton1Click:Connect(function()
        switchTab(name)
    end)
end

--// Toggle Utility
local function CreateToggle(name, parent, yPos, onToggle)
    local outer = Instance.new("Frame", parent)
    outer.Size = UDim2.new(0.9, 0, 0, 40)
    outer.Position = UDim2.new(0, 10, 0, yPos)
    outer.BackgroundColor3 = Color3.fromRGB(50, 50, 50)
    outer.BorderSizePixel = 0
    Instance.new("UICorner", outer).CornerRadius = UDim.new(0, 10)

    local label = Instance.new("TextLabel", outer)
    label.Text = name
    label.Size = UDim2.new(0.7, 0, 1, 0)
    label.Position = UDim2.new(0, 10, 0, 0)
    label.BackgroundTransparency = 1
    label.TextColor3 = Color3.fromRGB(200, 200, 200)
    label.Font = Enum.Font.Gotham
    label.TextSize = 14
    label.TextXAlignment = Enum.TextXAlignment.Left

    local button = Instance.new("TextButton", outer)
    button.Size = UDim2.new(0, 40, 0, 20)
    button.Position = UDim2.new(1, -50, 0.5, -10)
    button.BackgroundColor3 = Color3.fromRGB(35, 35, 35)
    button.Text = ""
    button.BorderSizePixel = 0
    Instance.new("UICorner", button).CornerRadius = UDim.new(1, 0)

    local state = false
    local fill = Instance.new("Frame", button)
    fill.Size = UDim2.new(0, 18, 0, 18)
    fill.Position = UDim2.new(0, 1, 0, 1)
    fill.BackgroundColor3 = Color3.fromRGB(100, 100, 100)
    fill.BorderSizePixel = 0
    Instance.new("UICorner", fill).CornerRadius = UDim.new(1, 0)

    button.MouseButton1Click:Connect(function()
        state = not state
        TweenService:Create(fill, TweenInfo.new(0.2), {
            Position = state and UDim2.new(1, -19, 0, 1) or UDim2.new(0, 1, 0, 1),
            BackgroundColor3 = state and Color3.fromRGB(0, 160, 255) or Color3.fromRGB(100, 100, 100)
        }):Play()
        if onToggle then onToggle(state) end
    end)
end

--// Slider Utility
local function CreateSlider(name, parent, yPos, min, max, default, onChange)
    local outer = Instance.new("Frame", parent)
    outer.Size = UDim2.new(0.9, 0, 0, 40)
    outer.Position = UDim2.new(0, 10, 0, yPos)
    outer.BackgroundColor3 = Color3.fromRGB(50, 50, 50)
    outer.BorderSizePixel = 0
    Instance.new("UICorner", outer).CornerRadius = UDim.new(0, 10)

    local label = Instance.new("TextLabel", outer)
    label.Text = name .. ": " .. tostring(default)
    label.Size = UDim2.new(0.7, 0, 1, 0)
    label.Position = UDim2.new(0, 10, 0, 0)
    label.BackgroundTransparency = 1
    label.TextColor3 = Color3.fromRGB(200, 200, 200)
    label.Font = Enum.Font.Gotham
    label.TextSize = 14
    label.TextXAlignment = Enum.TextXAlignment.Left

    local sliderTrack = Instance.new("Frame", outer)
    sliderTrack.Size = UDim2.new(0, 150, 0, 6)
    sliderTrack.Position = UDim2.new(1, -160, 0.5, -3)
    sliderTrack.BackgroundColor3 = Color3.fromRGB(35, 35, 35)
    sliderTrack.BorderSizePixel = 0
    Instance.new("UICorner", sliderTrack).CornerRadius = UDim.new(0, 3)

    local sliderFill = Instance.new("Frame", sliderTrack)
    sliderFill.Size = UDim2.new((default - min) / (max - min), 0, 1, 0)
    sliderFill.BackgroundColor3 = Color3.fromRGB(0, 160, 255)
    sliderFill.BorderSizePixel = 0
    Instance.new("UICorner", sliderFill).CornerRadius = UDim.new(0, 3)

    local sliderButton = Instance.new("TextButton", sliderTrack)
    sliderButton.Size = UDim2.new(0, 12, 0, 12)
    sliderButton.Position = UDim2.new((default - min) / (max - min), -6, 0, -3)
    sliderButton.BackgroundColor3 = Color3.fromRGB(200, 200, 200)
    sliderButton.BorderSizePixel = 0
    sliderButton.Text = ""
    Instance.new("UICorner", sliderButton).CornerRadius = UDim.new(0, 6)

    local dragging = false
    sliderButton.InputBegan:Connect(function(input)
        if input.UserInputType == Enum.UserInputType.MouseButton1 then
            dragging = true
        end
    end)

    sliderButton.InputEnded:Connect(function(input)
        if input.UserInputType == Enum.UserInputType.MouseButton1 then
            dragging = false
        end
    end)

    UserInputService.InputChanged:Connect(function(input)
        if dragging and input.UserInputType == Enum.UserInputType.MouseMovement then
            local mouseX = input.Position.X
            local trackAbsPos = sliderTrack.AbsolutePosition.X
            local trackAbsSize = sliderTrack.AbsoluteSize.X
            local relative = math.clamp((mouseX - trackAbsPos) / trackAbsSize, 0, 1)
            local value = math.floor(min + (max - min) * relative)
            sliderFill.Size = UDim2.new(relative, 0, 1, 0)
            sliderButton.Position = UDim2.new(relative, -6, 0, -3)
            label.Text = name .. ": " .. tostring(value)
            if onChange then onChange(value) end
        end
    end)
end

--// Spam Toggle Connection
local spamConnection

--// ESP Logic
local espConnections = {}
local abilityEspDrawings = {}
local ballEspDrawings = {}
local abilityEspGuis = {}

local function createAbilityESP(object)
    local gui = Instance.new("BillboardGui", CoreGui)
    gui.Name = "AbilityESP_" .. object:GetDebugId()
    gui.Adornee = object
    gui.Size = UDim2.new(0, 100, 0, 50)
    gui.StudsOffset = Vector3.new(0, 2, 0)
    gui.AlwaysOnTop = true

    local text = Instance.new("TextLabel", gui)
    text.Size = UDim2.new(1, 0, 1, 0)
    text.BackgroundTransparency = 1
    text.TextColor3 = Color3.fromRGB(0, 255, 0)
    text.Text = "Unknown"
    text.Font = Enum.Font.Gotham
    text.TextSize = 14
    abilityEspGuis[object] = gui

    local box = Drawing.new("Square")
    box.Thickness = 2
    box.Color = Color3.fromRGB(0, 255, 0)
    box.Filled = false
    box.Transparency = 0.8
    abilityEspDrawings[object] = box
end

local function createBallESP(object)
    local box = Drawing.new("Square")
    box.Thickness = 2
    box.Color = Color3.fromRGB(255, 0, 0)
    box.Filled = false
    box.Transparency = 0.8
    ballEspDrawings[object] = box
end

local function updateESP()
    local character = LocalPlayer.Character
    if not character or not character.PrimaryPart then
        for _, box in pairs(abilityEspDrawings) do
            box.Visible = false
        end
        for _, gui in pairs(abilityEspGuis) do
            gui.Enabled = false
        end
        for _, box in pairs(ballEspDrawings) do
            box.Visible = false
        end
        return
    end

    local playerPos = character.PrimaryPart.Position
    for _, ball in pairs(Workspace.Balls:GetChildren()) do
        local distance = (playerPos - ball.Position).Magnitude
        if getgenv().abilityESP and distance <= (getgenv().abilityESPDistance or 5000) then
            if ball:GetAttribute("realBall") and ball:FindFirstChild("zoomies") then
                if not abilityEspDrawings[ball] then
                    createAbilityESP(ball)
                end
                local box = abilityEspDrawings[ball]
                local gui = abilityEspGuis[ball]
                local vector, onScreen = workspace.CurrentCamera:WorldToViewportPoint(ball.Position)
                if onScreen then
                    local size = (ball.Size.X * 1000) / vector.Z
                    box.Size = Vector2.new(size, size)
                    box.Position = Vector2.new(vector.X - size / 2, vector.Y - size / 2)
                    box.Visible = true
                    gui.Enabled = true

                    -- Find nearest player as a proxy for ability owner
                    local closestPlayer = nil
                    local shortest = math.huge
                    for _, player in pairs(Players:GetPlayers()) do
                        if player ~= LocalPlayer and player.Character and player.Character.PrimaryPart then
                            local playerDist = (player.Character.PrimaryPart.Position - ball.Position).Magnitude
                            if playerDist < shortest then
                                shortest = playerDist
                                closestPlayer = player
                            end
                        end
                    end
                    if gui and closestPlayer then
                        gui.TextLabel.Text = closestPlayer.Name .. "'s Ability"
                    end
                else
                    box.Visible = false
                    gui.Enabled = false
                end
            end
        else
            if abilityEspDrawings[ball] then
                abilityEspDrawings[ball].Visible = false
            end
            if abilityEspGuis[ball] then
                abilityEspGuis[ball].Enabled = false
            end
        end

        if getgenv().ballESP and distance <= (getgenv().abilityESPDistance or 5000) then
            if not ballEspDrawings[ball] then
                createBallESP(ball)
            end
            local box = ballEspDrawings[ball]
            local vector, onScreen = workspace.CurrentCamera:WorldToViewportPoint(ball.Position)
            if onScreen then
                local size = (ball.Size.X * 1000) / vector.Z
                box.Size = Vector2.new(size, size)
                box.Position = Vector2.new(vector.X - size / 2, vector.Y - size / 2)
                box.Visible = true
            else
                box.Visible = false
            end
        else
            if ballEspDrawings[ball] then
                ballEspDrawings[ball].Visible = false
            end
        end
    end
end

local function cleanupESP()
    for _, box in pairs(abilityEspDrawings) do
        box:Remove()
    end
    for _, box in pairs(ballEspDrawings) do
        box:Remove()
    end
    for _, gui in pairs(abilityEspGuis) do
        gui:Destroy()
    end
    abilityEspDrawings = {}
    ballEspDrawings = {}
    abilityEspGuis = {}
    for _, conn in pairs(espConnections) do
        conn:Disconnect()
    end
    espConnections = {}
end

espConnections[#espConnections + 1] = RunService.RenderStepped:Connect(function()
    updateESP()
end)

espConnections[#espConnections + 1] = Workspace.Balls.ChildRemoved:Connect(function(child)
    if abilityEspDrawings[child] then
        abilityEspDrawings[child]:Remove()
        abilityEspDrawings[child] = nil
    end
    if ballEspDrawings[child] then
        ballEspDrawings[child]:Remove()
        ballEspDrawings[child] = nil
    end
    if abilityEspGuis[child] then
        abilityEspGuis[child]:Destroy()
        abilityEspGuis[child] = nil
    end
end)

--// Populate Tabs
local function populateTab(tabName)
    local page = tabs[tabName]
    if tabName == "Main" then
CreateToggle("Auto Parry", page, 10, function(state)
    if state then
        task.spawn(function()
            repeat task.wait() until game:IsLoaded()

            local Players = game:GetService("Players")
            local RunService = game:GetService("RunService")
            local UserInputService = game:GetService("UserInputService")
            local Workspace = game:GetService("Workspace")
            local ReplicatedStorage = game:GetService("ReplicatedStorage")
            local TweenService = game:GetService("TweenService")
            local Debris = game:GetService("Debris")
            local VirtualInputManager = game:GetService("VirtualInputManager")
            local LocalPlayer = Players.LocalPlayer
            local Character = LocalPlayer.Character or LocalPlayer.CharacterAdded:Wait()
            local Humanoid = Character:FindFirstChildOfClass("Humanoid")

            local Alive = Workspace:FindFirstChild("Alive")
            local Aerodynamic = false
            local Aerodynamic_Time = tick()
            local Last_Input = UserInputService:GetLastInputType()
            local Vector2_Mouse_Location = nil
            local Grab_Parry = nil
            local Parry_Key = nil
            local Remotes = {}
            local revertedRemotes = {}
            local originalMetatables = {}
            local Parries = 0
            local Connections_Manager = {}
            local Animation = {storage = {}, current = nil, track = nil}

            setfpscap(60)

            local function isValidRemoteArgs(args)
                return #args == 7 and
                    type(args[2]) == "string" and
                    type(args[3]) == "number" and
                    typeof(args[4]) == "CFrame" and
                    type(args[5]) == "table" and
                    type(args[6]) == "table" and
                    type(args[7]) == "boolean"
            end

            local function hookRemote(remote)
                if not revertedRemotes[remote] then
                    if not originalMetatables[getmetatable(remote)] then
                        originalMetatables[getmetatable(remote)] = true
                        local meta = getrawmetatable(remote)
                        setreadonly(meta, false)
                        local oldIndex = meta.__index
                        meta.__index = function(self, key)
                            if (key == "FireServer" and self:IsA("RemoteEvent")) or (key == "InvokeServer" and self:IsA("RemoteFunction")) then
                                return function(_, ...)
                                    local args = {...}
                                    if isValidRemoteArgs(args) then
                                        if not revertedRemotes[self] then
                                            revertedRemotes[self] = args
                                            local remoteType = self:IsA("RemoteEvent") and "RemoteEvent" or "RemoteFunction"
                                            local remoteData = {
                                                RemoteName = self.Name,
                                                RemoteType = remoteType,
                                                Args = args
                                            }
                                            setclipboard(game:GetService("HttpService"):JSONEncode(remoteData))
                                            game.StarterGui:SetCore("SendNotification", {
                                                Title = "Loaded",
                                                Text = "auto parry and auto spam loaded",
                                                Duration = 1,
                                            })
                                        end
                                    end
                                    return oldIndex(self, key)(_, unpack(args))
                                end
                            end
                            return oldIndex(self, key)
                        end
                        setreadonly(meta, true)
                    end
                end
            end

            for _, remote in pairs(game.ReplicatedStorage:GetChildren()) do
                if remote:IsA("RemoteEvent") or remote:IsA("RemoteFunction") then
                    hookRemote(remote)
                end
            end

            game.ReplicatedStorage.ChildAdded:Connect(function(child)
                if child:IsA("RemoteEvent") or child:IsA("RemoteFunction") then
                    hookRemote(child)
                end
            end)

            local function createAnimation(object, info, value)
                local animation = TweenService:Create(object, info, value)
                animation:Play()
                task.wait(info.Time)
                Debris:AddItem(animation, 0)
                animation:Destroy()
            end

            for _, animation in pairs(ReplicatedStorage.Misc.Emotes:GetChildren()) do
                if (animation:IsA("Animation") and animation:GetAttribute("EmoteName")) then
                    local emoteName = animation:GetAttribute("EmoteName")
                    Animation.storage[emoteName] = animation
                end
            end

            local Auto_Parry = {}
            Auto_Parry.Parry_Animation = function()
                local Parry_Animation = ReplicatedStorage.Shared.SwordAPI.Collection.Default:FindFirstChild("GrabParry")
                local Current_Sword = LocalPlayer.Character:GetAttribute("CurrentlyEquippedSword")
                if (not Current_Sword or not Parry_Animation) then
                    return
                end
                local Sword_Data = ReplicatedStorage.Shared.ReplicatedInstances.Swords.GetSword:Invoke(Current_Sword)
                if (not Sword_Data or not Sword_Data['AnimationType']) then
                    return
                end
                for _, object in pairs(ReplicatedStorage.Shared.SwordAPI.Collection:GetChildren()) do
                    if (object.Name == Sword_Data['AnimationType']) then
                        local sword_animation_type = (object:FindFirstChild("GrabParry") and "GrabParry") or "Grab"
                        Parry_Animation = object[sword_animation_type]
                    end
                end
                Grab_Parry = LocalPlayer.Character.Humanoid.Animator:LoadAnimation(Parry_Animation)
                Grab_Parry:Play()
            end

            Auto_Parry.Play_Animation = function(animationName)
                local Animations = Animation.storage[animationName]
                if not Animations then
                    return false
                end
                local Animator = LocalPlayer.Character.Humanoid.Animator
                if (Animation.track and Animation.track:IsA("AnimationTrack")) then
                    Animation.track:Stop()
                end
                Animation.track = Animator:LoadAnimation(Animations)
                if (Animation.track and Animation.track:IsA("AnimationTrack")) then
                    Animation.track:Play()
                end
                Animation.current = animationName
            end

            Auto_Parry.Get_Balls = function()
                local Balls = {}
                for _, instance in pairs(Workspace.Balls:GetChildren()) do
                    if instance:GetAttribute("realBall") then
                        instance.CanCollide = false
                        table.insert(Balls, instance)
                    end
                end
                return Balls
            end

            Auto_Parry.Get_Ball = function()
                for _, instance in pairs(Workspace.Balls:GetChildren()) do
                    if instance:GetAttribute("realBall") then
                        instance.CanCollide = false
                        return instance
                    end
                end
            end

            Auto_Parry.Parry_Data = function()
                local Events = {}
                local Camera = workspace.CurrentCamera
                if ((Last_Input == Enum.UserInputType.MouseButton1) or (Last_Input == Enum.UserInputType.MouseButton2) or (Last_Input == Enum.UserInputType.Keyboard)) then
                    local Mouse_Location = UserInputService:GetMouseLocation()
                    Vector2_Mouse_Location = {Mouse_Location.X, Mouse_Location.Y}
                else
                    Vector2_Mouse_Location = {(Camera.ViewportSize.X / 2), (Camera.ViewportSize.Y / 2)}
                end
                for _, v in pairs(workspace.Alive:GetChildren()) do
                    if (v:IsA("Model") and v.PrimaryPart) then
                        Events[tostring(v)] = Camera:WorldToScreenPoint(v.PrimaryPart.Position)
                    end
                end
                return {0, Camera.CFrame, Events, Vector2_Mouse_Location}
            end

            local FirstParryDone = false
            function Auto_Parry.Parry()
                local Parry_Data = Auto_Parry.Parry_Data()
                if not FirstParryDone then
                    VirtualInputManager:SendKeyEvent(true, Enum.KeyCode.F, false, game)
                    task.wait(0.1)
                    VirtualInputManager:SendKeyEvent(false, Enum.KeyCode.F, false, game)
                    FirstParryDone = true
                else
                    for remote, args in pairs(revertedRemotes) do
UIStroke
                        if remote:IsA("RemoteEvent") then
                            remote:FireServer(unpack(args))
                        elseif remote:IsA("RemoteFunction") then
                            remote:InvokeServer(unpack(args))
                        end
                    end
                end
                if Parries > 7 then
                    return false
                end
                Parries += 1
                task.delay(0.5, function()
                    if Parries > 0 then
                        Parries -= 1
                    end
                end)
            end

            local Lerp_Radians = 0
            local Last_Warping = tick()
            Auto_Parry.Linear_Interpolation = function(a, b, time_volume)
                return a + ((b - a) * time_volume)
            end

            local Previous_Velocity = {}
            local Curving = tick()
            Auto_Parry.Is_Curved = function()
                local Ball = Auto_Parry.Get_Ball()
                if not Ball then
                    return false
                end
                local Zoomies = Ball:FindFirstChild("zoomies")
                if not Zoomies then
                    return false
                end
                local Ping = game:GetService("Stats").Network.ServerStatsItem["Data Ping"]:GetValue()
                local Velocity = Zoomies.VectorVelocity
                local Ball_Direction = Velocity.Unit
                local Direction = (LocalPlayer.Character.PrimaryPart.Position - Ball.Position).Unit
                local Dot = Direction:Dot(Ball_Direction)
                local Speed = Velocity.Magnitude
                local Speed_Threshold = math.min(Speed / 100, 40)
                local Angle_Threshold = 40 * math.max(Dot, 0)
                local Direction_Difference = (Ball_Direction - Velocity).Unit
                local Direction_Similarity = Direction:Dot(Direction_Difference)
                local Dot_Difference = Dot - Direction_Similarity
                local Dot_Threshold = 0.5 - (Ping / 1000)
                local Distance = (LocalPlayer.Character.PrimaryPart.Position - Ball.Position).Magnitude
                local Reach_Time = (Distance / Speed) - (Ping / 1000)
                local Enough_Speed = Speed > 100
                local Ball_Distance_Threshold = (15 - math.min(Distance / 1000, 15)) + Angle_Threshold + Speed_Threshold
                table.insert(Previous_Velocity, Velocity)
                if (#Previous_Velocity > 4) then
                    table.remove(Previous_Velocity, 1)
                end
                if (Enough_Speed and (Reach_Time > (Ping / 10))) then
                    Ball_Distance_Threshold = math.max(Ball_Distance_Threshold - 15, 15)
                end
                if (Distance < Ball_Distance_Threshold) then
                    return false
                end
                if ((tick() - Curving) < (Reach_Time / 1.5)) then
                    return true
                end
                if (Dot_Difference < Dot_Threshold) then
                    return true
                end
                local Radians = math.asin(Dot)
                Lerp_Radians = Auto_Parry.Linear_Interpolation(Lerp_Radians, Radians, 0.8)
                if (Lerp_Radians < 0.018) then
                    Last_Warping = tick()
                end
                if ((tick() - Last_Warping) < (Reach_Time / 1.5)) then
                    return true
                end
                if (#Previous_Velocity == 4) then
                    for i = 1, 2 do
                        local Intended_Direction_Difference = (Ball_Direction - Previous_Velocity[i].Unit).Unit
                        local Intended_Dot = Direction:Dot(Intended_Direction_Difference)
                        local Intended_Dot_Difference = Dot - Intended_Dot
                        if (Intended_Dot_Difference < Dot_Threshold) then
                            return true
                        end
                    end
                end
                return Dot < Dot_Threshold
            end

            Auto_Parry.Closest_Player = function()
                local Max_Distance = math.huge
                local Closest_Entity = nil
                for _, Entity in pairs(Workspace.Alive:GetChildren()) do
                    if ((tostring(Entity) ~= tostring(LocalPlayer)) and Entity.PrimaryPart) then
                        local Distance = LocalPlayer:DistanceFromCharacter(Entity.PrimaryPart.Position)
                        if (Distance < Max_Distance) then
                            Max_Distance = Distance
                            Closest_Entity = Entity
                        end
                    end
                end
                return Closest_Entity
            end

            Auto_Parry.Get_Ball_Properties = function()
                local ball = Auto_Parry.Get_Ball()
                if not ball then
                    return false
                end
                local character = LocalPlayer.Character
                if (not character or not character.PrimaryPart) then
                    return false
                end
                local ballVelocity = ball.AssemblyLinearVelocity
                local ballDirection = (character.PrimaryPart.Position - ball.Position).Unit
                local ballDistance = (character.PrimaryPart.Position - ball.Position).Magnitude
                local ballDot = ballDirection:Dot(ballVelocity.Unit)
                return {Velocity=ballVelocity, Direction=ballDirection, Distance=ballDistance, Dot=ballDot}
            end

            Connections_Manager["Auto Parry"] = RunService.PreSimulation:Connect(function()
                local One_Ball = Auto_Parry.Get_Ball()
                local Balls = Auto_Parry.Get_Balls()
                if (not Balls or (#Balls == 0)) then
                    return
                end
                for _, Ball in pairs(Balls) do
                    if not Ball then
                        return
                    end
                    local Zoomies = Ball:FindFirstChild("zoomies")
                    if not Zoomies then
                        return
                    end
                    Ball:GetAttributeChangedSignal("target"):Once(function()
                        Parried = false
                    end)
                    if Parried then
                        return
                    end
                    local Ball_Target = Ball:GetAttribute("target")
                    local One_Target = One_Ball and One_Ball:GetAttribute("target")
                    local Velocity = Zoomies.VectorVelocity
                    local character = LocalPlayer.Character
                    if (not character or not character.PrimaryPart) then
                        return
                    end
                    local Distance = (character.PrimaryPart.Position - Ball.Position).Magnitude
                    local Speed = Velocity.Magnitude
                    local Ping = game:GetService("Stats").Network.ServerStatsItem["Data Ping"]:GetValue() / 10
                    local Parry_Accuracy = (Speed / 3.25) + Ping
                    local Curved = Auto_Parry.Is_Curved()
                    if ((Ball_Target == tostring(LocalPlayer)) and Aerodynamic) then
                        local Elapsed_Tornado = tick() - Aerodynamic_Time
                        if (Elapsed_Tornado > 0.6) then
                            Aerodynamic_Time = tick()
                            Aerodynamic = false
                        end
                        return
                    end
                    if ((One_Target == tostring(LocalPlayer)) and Curved) then
                        return
                    end
                    if ((Ball_Target == tostring(LocalPlayer)) and (Distance <= Parry_Accuracy)) then
                        Auto_Parry.Parry()
                        Parried = true
                    end
                    local Last_Parrys = tick()
                    while (tick() - Last_Parrys) < 1 do
                        if not Parried then
                            break
                        end
                        task.wait()
                    end
                    Parried = false
                end
            end)
        end)
    else
        if Connections_Manager["Auto Parry"] then
            Connections_Manager["Auto Parry"]:Disconnect()
            Connections_Manager["Auto Parry"] = nil
        end
    end
end)
CreateToggle("Auto Spam", page, 60, function(state)
    if state then
        task.spawn(function()
            repeat task.wait() until game:IsLoaded()

            local Players = game:GetService("Players")
            local RunService = game:GetService("RunService")
            local UserInputService = game:GetService("UserInputService")
            local Workspace = game:GetService("Workspace")
            local VirtualInputManager = game:GetService("VirtualInputManager")
            local LocalPlayer = Players.LocalPlayer
            local Character = LocalPlayer.Character or LocalPlayer.CharacterAdded:Wait()

            -- Validate required services and objects
            if not Workspace or not LocalPlayer or not Character or not VirtualInputManager then
                warn("Required services or objects are missing. Auto Spam disabled.")
                return
            end

            local Auto_Parry = {}
            Auto_Parry.Get_Ball = function()
                if not Workspace:FindFirstChild("Balls") then return nil end
                for _, instance in pairs(Workspace.Balls:GetChildren()) do
                    if instance:GetAttribute("realBall") then
                        instance.CanCollide = false
                        return instance
                    end
                end
                return nil
            end
            Auto_Parry.Closest_Player = function()
                local Max_Distance = math.huge
                local Closest_Entity = nil
                if not Workspace:FindFirstChild("Alive") then return nil end
                for _, Entity in pairs(Workspace.Alive:GetChildren()) do
                    if tostring(Entity) ~= tostring(LocalPlayer) and Entity:FindFirstChild("PrimaryPart") then
                        local Distance = LocalPlayer:DistanceFromCharacter(Entity.PrimaryPart.Position)
                        if Distance and Distance < Max_Distance then
                            Max_Distance = Distance
                            Closest_Entity = Entity
                        end
                    end
                end
                return Closest_Entity
            end
            Auto_Parry.Get_Ball_Properties = function()
                local ball = Auto_Parry.Get_Ball()
                if not ball or not Character:FindFirstChild("PrimaryPart") then return false end
                local ballVelocity = ball.AssemblyLinearVelocity
                local ballDirection = (Character.PrimaryPart.Position - ball.Position).Unit
                local ballDistance = (Character.PrimaryPart.Position - ball.Position).Magnitude
                local ballDot = ballDirection:Dot(ballVelocity.Unit)
                return {Velocity=ballVelocity, Direction=ballDirection, Distance=ballDistance, Dot=ballDot}
            end
            Auto_Parry.Get_Entity_Properties = function()
                local entity = Auto_Parry.Closest_Player()
                if not entity or not entity:FindFirstChild("PrimaryPart") or not Character:FindFirstChild("PrimaryPart") then
                    return false
                end
                local entityVelocity = entity.PrimaryPart.Velocity
                local entityDirection = (Character.PrimaryPart.Position - entity.PrimaryPart.Position).Unit
                local entityDistance = (Character.PrimaryPart.Position - entity.PrimaryPart.Position).Magnitude
                return {Velocity=entityVelocity, Direction=entityDirection, Distance=entityDistance}
            end
            Auto_Parry.Parry = function()
                if VirtualInputManager then
                    VirtualInputManager:SendKeyEvent(true, Enum.KeyCode.F, false, game)
                    task.wait(0.1)
                    VirtualInputManager:SendKeyEvent(false, Enum.KeyCode.F, false, game)
                end
            end

            local autoSpamCoroutine = nil
            local targetPlayer = nil
            autoSpamCoroutine = coroutine.create(function(signal)
                while state and (signal ~= "stop") do
                    local ball = Auto_Parry.Get_Ball()
                    if not ball or not ball:IsDescendantOf(workspace) then
                        task.wait(0.1)
                        continue
                    end
                    local zoomies = ball:FindFirstChild("zoomies")
                    if not zoomies then
                        task.wait(0.1)
                        continue
                    end
                    local entity = Auto_Parry.Closest_Player()
                    if not entity or not entity:IsDescendantOf(workspace) then
                        task.wait(0.1)
                        continue
                    end
                    local playerDistance = LocalPlayer:DistanceFromCharacter(ball.Position)
                    local targetDistance = LocalPlayer:DistanceFromCharacter(entity.PrimaryPart.Position)

                    if zoomies and zoomies.Parent == ball and (playerDistance <= 50 or targetDistance <= 50) then
                        Auto_Parry.Parry()
                    end
                    task.wait(0.05)
                end
            end)
            coroutine.resume(autoSpamCoroutine)
        end)
    else
        if autoSpamCoroutine then
            coroutine.resume(autoSpamCoroutine, "stop")
            autoSpamCoroutine = nil
        end
    end
end)
    elseif tabName == "ESP" then
        CreateToggle("Ball ESP", page, 10, function(state)
            getgenv().abilityESP = state
        end)
        CreateSlider("ESP Distance", page, 60, 0, 10000, 5000, function(value)
            getgenv().abilityESPDistance = value
        end)
    elseif tabName == "skin Changer" then
        local yPos = 110
        -- Toggle for Skin Changer
        local outerToggle = Instance.new("Frame", page)
        outerToggle.Size = UDim2.new(0.9, 0, 0, 40)
        outerToggle.Position = UDim2.new(0, 10, 0, yPos)
        outerToggle.BackgroundColor3 = Color3.fromRGB(50, 50, 50)
        outerToggle.BorderSizePixel = 0
        Instance.new("UICorner", outerToggle).CornerRadius = UDim.new(0, 10)

        local labelToggle = Instance.new("TextLabel", outerToggle)
        labelToggle.Text = "Skin Changer"
        labelToggle.Size = UDim2.new(0.7, 0, 1, 0)
        labelToggle.Position = UDim2.new(0, 10, 0, 0)
        labelToggle.BackgroundTransparency = 1
        labelToggle.TextColor3 = Color3.fromRGB(200, 200, 200)
        labelToggle.Font = Enum.Font.Gotham
        labelToggle.TextSize = 14
        labelToggle.TextXAlignment = Enum.TextXAlignment.Left

        local buttonToggle = Instance.new("TextButton", outerToggle)
        buttonToggle.Size = UDim2.new(0, 40, 0, 20)
        buttonToggle.Position = UDim2.new(1, -50, 0.5, -10)
        buttonToggle.BackgroundColor3 = Color3.fromRGB(35, 35, 35)
        buttonToggle.Text = ""
        buttonToggle.BorderSizePixel = 0
        Instance.new("UICorner", buttonToggle).CornerRadius = UDim.new(1, 0)

        local state = false
        local fillToggle = Instance.new("Frame", buttonToggle)
        fillToggle.Size = UDim2.new(0, 18, 0, 18)
        fillToggle.Position = UDim2.new(0, 1, 0, 1)
        fillToggle.BackgroundColor3 = Color3.fromRGB(100, 100, 100)
        fillToggle.BorderSizePixel = 0
        Instance.new("UICorner", fillToggle).CornerRadius = UDim.new(1, 0)

        buttonToggle.MouseButton1Click:Connect(function()
            state = not state
            TweenService:Create(fillToggle, TweenInfo.new(0.2), {
                Position = state and UDim2.new(1, -19, 0, 1) or UDim2.new(0, 1, 0, 1),
                BackgroundColor3 = state and Color3.fromRGB(0, 160, 255) or Color3.fromRGB(100, 100, 100)
            }):Play()
            getgenv().skinChanger = state
            if state then
                getgenv().updateSword()
            end
        end)

        -- Warning Paragraph
        local warningFrame = Instance.new("Frame", page)
        warningFrame.Size = UDim2.new(0.9, 0, 0, 80)
        warningFrame.Position = UDim2.new(0, 10, 0, yPos + 50)
        warningFrame.BackgroundColor3 = Color3.fromRGB(50, 50, 50)
        warningFrame.BorderSizePixel = 0
        Instance.new("UICorner", warningFrame).CornerRadius = UDim.new(0, 10)

        local warningLabel = Instance.new("TextLabel", warningFrame)
        warningLabel.Text = "⚠️ EVERYONE CAN SEE ANIMATIONS\nIF YOU USE SKIN CHANGER BACKSWORD YOU MUST EQUIP AN ACTUAL BACKSWORD (Feature broken)"
        warningLabel.Size = UDim2.new(1, -20, 1, -20)
        warningLabel.Position = UDim2.new(0, 10, 0, 10)
        warningLabel.BackgroundTransparency = 1
        warningLabel.TextColor3 = Color3.fromRGB(220, 220, 220)
        warningLabel.Font = Enum.Font.GothamSemibold
        warningLabel.TextSize = 12
        warningLabel.TextWrapped = true
        warningLabel.TextYAlignment = Enum.TextYAlignment.Top
        warningLabel.TextXAlignment = Enum.TextXAlignment.Left

        -- Textbox for Skin Name
        local textboxFrame = Instance.new("Frame", page)
        textboxFrame.Size = UDim2.new(0.9, 0, 0, 40)
        textboxFrame.Position = UDim2.new(0, 10, 0, yPos + 140)
        textboxFrame.BackgroundColor3 = Color3.fromRGB(50, 50, 50)
        textboxFrame.BorderSizePixel = 0
        Instance.new("UICorner", textboxFrame).CornerRadius = UDim.new(0, 10)

        local textboxLabel = Instance.new("TextLabel", textboxFrame)
        textboxLabel.Text = "🗡 Skin Name (Case Sensitive)"
        textboxLabel.Size = UDim2.new(0.7, 0, 1, 0)
        textboxLabel.Position = UDim2.new(0, 10, 0, 0)
        textboxLabel.BackgroundTransparency = 1
        textboxLabel.TextColor3 = Color3.fromRGB(200, 200, 200)
        textboxLabel.Font = Enum.Font.Gotham
        textboxLabel.TextSize = 14
        textboxLabel.TextXAlignment = Enum.TextXAlignment.Left

        local textbox = Instance.new("TextBox", textboxFrame)
        textbox.Size = UDim2.new(0, 150, 0, 24)
        textbox.Position = UDim2.new(1, -160, 0.5, -12)
        textbox.BackgroundColor3 = Color3.fromRGB(35, 35, 35)
        textbox.TextColor3 = Color3.fromRGB(200, 200, 200)
        textbox.PlaceholderText = "Enter Sword Skin Name..."
        textbox.Font = Enum.Font.Gotham
        textbox.TextSize = 12
        textbox.BorderSizePixel = 0
        Instance.new("UICorner", textbox).CornerRadius = UDim.new(0, 8)
        textbox.ClearTextOnFocus = false

        textbox.FocusLost:Connect(function(enterPressed)
            if enterPressed then
                local text = textbox.Text
                getgenv().swordModel = text
                getgenv().swordAnimations = text
                getgenv().swordFX = text
                if getgenv().skinChanger then
                    getgenv().updateSword()
                end
            end
        end)
    elseif tabName == "Home" then
        local box = Instance.new("Frame", page)
        box.Size = UDim2.new(0.9, 0, 0, 120)
        box.Position = UDim2.new(0, 20, 0, 20)
        box.BackgroundColor3 = Color3.fromRGB(40, 40, 40)
        box.BorderSizePixel = 0
        Instance.new("UICorner", box).CornerRadius = UDim.new(0, 10)

        local lbl = Instance.new("TextLabel", box)
        lbl.Text = "📢 Update v1.2.4\n- Added auto spam and auto parry\n- Improved UI\n- Many UI items do not full work yet"
        lbl.Size = UDim2.new(1, -20, 1, -20)
        lbl.Position = UDim2.new(0, 10, 0, 10)
        lbl.BackgroundTransparency = 1
        lbl.TextColor3 = Color3.fromRGB(220, 220, 220)
        lbl.Font = Enum.Font.GothamSemibold
        lbl.TextSize = 14
        lbl.TextWrapped = true
        lbl.TextYAlignment = Enum.TextYAlignment.Top
        lbl.TextXAlignment = Enum.TextXAlignment.Left
    end
end

for _, tabName in ipairs(tabNames) do
    populateTab(tabName)
end

-- Hide UI with backtick
UserInputService.InputBegan:Connect(function(input, gpe)
    if gpe then return end
    if input.KeyCode == Enum.KeyCode.Backquote then
        main.Visible = not main.Visible
    end
end)

-- Toggle Button (Open/Close)
local toggleBtn = Instance.new("TextButton", gui)
toggleBtn.Name = "ToggleButton"
toggleBtn.Size = UDim2.new(0, 80, 0, 40)
toggleBtn.Position = UDim2.new(0, 10, 0.5, -20)
toggleBtn.BackgroundColor3 = Color3.fromRGB(40, 40, 40)
toggleBtn.TextColor3 = Color3.fromRGB(200, 200, 200)
toggleBtn.Font = Enum.Font.GothamBold
toggleBtn.TextSize = 16
toggleBtn.BorderSizePixel = 0
toggleBtn.ZIndex = 5
Instance.new("UICorner", toggleBtn).CornerRadius = UDim.new(0, 20)

local minimized = false
local dragging, dragInput, dragStart, startPos

local function clampPosition(pos, btnSize, viewportSize)
    return Vector2.new(
        math.clamp(pos.X, 0, viewportSize.X - btnSize.X),
        math.clamp(pos.Y, 0, viewportSize.Y - btnSize.Y)
    )
end

local function update(input)
    local delta = input.Position - dragStart
    local newPos = UDim2.new(0, startPos.X.Offset + delta.X, 0, startPos.Y.Offset + delta.Y)
    local viewportSize = workspace.CurrentCamera.ViewportSize
    local absSize = toggleBtn.AbsoluteSize
    local clampedPos = clampPosition(Vector2.new(newPos.X.Offset, newPos.Y.Offset), absSize, viewportSize)
    toggleBtn.Position = UDim2.new(0, clampedPos.X, 0, clampedPos.Y)
end

toggleBtn.InputBegan:Connect(function(input)
    if input.UserInputType == Enum.UserInputType.MouseButton1 then
        dragging = true
        dragStart = input.Position
        startPos = toggleBtn.Position
        input.Changed:Connect(function()
            if input.UserInputState == Enum.UserInputState.End then
                dragging = false
            end
        end)
    end
end)

toggleBtn.InputChanged:Connect(function(input)
    if input.UserInputType == Enum.UserInputType.MouseMovement then
        dragInput = input
    end
end)

UserInputService.InputChanged:Connect(function(input)
    if input == dragInput and dragging then
        update(input)
    end
end)

toggleBtn.MouseButton1Click:Connect(function()
    minimized = not minimized
    main.Visible = not minimized
    toggleBtn.Text = minimized and "Open" or "Close"
end)

toggleBtn.Text = "Close"
switchTab(activeTab)
