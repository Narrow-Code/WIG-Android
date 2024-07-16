# WIG

## Table of Contents
1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
   - [Android Installation](#android-installation)
   - [Self-Hosting Setup](#self-host-setup)
   - [Sign-Up](#sign-up)
   - [Login](#login)
3. [Ownership Features](#ownership-features)
   - [Adding New Ownership](#adding-new-ownership)
4. [Appendix](#appendix)

---

## Introduction <a name="introduction"></a>
- WIG is a self-hosted inventory management service to remember "What I Got".
- It is built for the everyday person in need of organization.
- Utilizing barcode and QR scanning, its UI/UX enables easy access to ownership items and locations.
- WIG gives the ability to store items into locations, to find items easily via the mobile app.
- Item inventory tracking makes checking supply stock flawless from a distance.

---

## Getting Started <a name="getting-started"></a>

### Android Installation <a name="android-installation"></a>
- Download the most recent Android APK from [GitHub Releases](https://github.com/Narrow-Code/WIG-Android/releases)
- Open file on Android device and allow installation from unknown source

### Self-Hosting Setup<a name="self-host-setup"></a>
- **If you do not plan on Self-Hosting, this setup may be skipped**
- If you plan on Self-Hosting please resort to the documentation on the [WIG-Server](https://github.com/Narrow-Code/WIG-Server) GitHub page.
- Once WIG-Server is set up and running you may procceed to open the WIG-Android application.
- **IMPORTANT: DO NOT LOGIN OR SIGN-UP BEFORE COMPLETING NEXT STEPS**
- Click on **gear/settings** icon in upper right hand corner of Login screen.
- Enter in your servers **Hostname** and **Port**
- Click **Connect**
- If you are using your server for the first time you will [Sign-Up](#sign-up) as usual.
- If you have already used your server before you can procceed to [Login](#login)

### Sign-Up<a name="sign-up"></a>
- On the **Login** page, click the **Sign-Up** button
- Fill out all mandatory fields and select **Sign-Up**
- If user is not Self-Hosting a confirmation email will be sent.
- Check email and click on the confirmation link.
- You will now be able to [Login](#login)

### Login<a name="login"></a>
- On the **Login** page, fill out **Username** and **Password**
- Click **Login**
---

## Ownership Features <a name="ownership-features"></a>

### Adding New Ownership <a name="adding-new-ownership"></a>
- There are two ways to add an Ownership to the inventory.
   1. **Scan Barcode**
      - In Scanner View, use the camera and aim it at items barcode
      - The Scanner will auto detect and send to the server
      - If the item exists as an Ownership it will return existing ownership
      - If it does not exist, it will check item database for existing item information
      - It will then return the created Ownership in the Ownership Table bellow the camera
      - **TODO:** If the item does not exist in database it will prompt open the Manually Add option

   2. **Manually Add**
      - In Scanner View, click on the Plus + icon on the top right of the Ownership table
      - Enter the item's Name and a QR-Code or Barcode number to associate with it
      - Select **Item** from the dropdown menu
      - Click **Create*
      - The item will then be added to your inventory and can be scanned for later use
 
---

## Appendix <a name="appendix"></a>
- **Item**: Define item
- **Ownership**: Define ownership
- **Location**: Define location
- **Borrower**: Define borrower

