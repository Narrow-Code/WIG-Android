# WIG - Android

## Table of Contents
1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
   - [Android Installation](#android-installation)
   - [Self-Hosting Setup](#self-host-setup)
   - [Sign-Up](#sign-up)
   - [Login](#login)
3. [Ownership Table Features](#ownership-table-features)
   - [Add New Ownership](#add-new-ownership)
   - [Edit Ownership](#edit-ownership)
   - [Remove Ownership](#remove-ownership)
   - [Clear Ownership](#clear-ownerships)
   - [Search Ownerships](#search-ownerships)
   - [Place Ownerships](#place-ownerships)
   - [Checkout Ownership](#checkout-ownerships)
   - [Change Quantity](#change-quantity)
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

## Ownership Table Features <a name="ownership-table-features"></a>
Ownership Table is able to be accessed via the Scanner View page, by clicking on the QR code icon, located in the top right hand corner of the app.

### Add New Ownership <a name="add-new-ownership"></a>
There are two ways to add an Ownership to the inventory.
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
   
### Edit Ownership <a name="edit-ownership"></a>
An Ownership can be edited to change to following fields:
- Name: This is the display name to appear on any table
- QR: This is the customized code that can be scanned to retrieve the Ownership
- Tags: A keyword or term assigned for search criteria
- Notes/Description: Customized notes as self reminders about the Ownership

**How to Edit**
- In Scanner View have Ownership table selected
- Have Ownership added to table
- Click on the Ownership you wish to edit
- Fill out the fields to edit
- Click Save

### Remove Ownership <a name="remove-ownership"></a>
Removing an Ownership only removes it from the Ownership table. It does not Delete it from inventory.

**How to Remove Single Ownership**
- In Scanner View have Ownership table selected
- Long hold click on the Ownership to remove
- Confirm the Ownershipi

### Clear Ownerships <a name="clear-ownerships"></a>
Clearing Ownerships removes all Ownerships from the Ownership table. It does not Delete any from the inventory.

**How to Clear Ownerships**
- In Scanner View have Ownership table selected
- Click the **CLEAR** button toward the bottom left of the table

### Search Ownerships <a name="search-ownerships"></a>
- In Scanner View have Ownership table selected
- Click on magnifying glass icon toward upper right hand side of Ownership table
- Enter search criteria and select Search
- Click on the Ownership you would like to add to the table
- Click the Add button in the prompt
- Click Cancel button at the bottom of the search popup
- Ownership will be added to the table

### Place Ownerships <a name="place-ownerships"></a>
To place an Ownership is the action of setting a direct connection between a Location and an Ownership. 
This would be used to store items inside of a location. 
For example, if Luke stored a roll of paper towels in a grey bin, Luke would "place" the paper towels in the grey bin location.

**How to Place Ownerships**
- At least one Ownership must be present in the Ownerships Table
- A Location must be present in the Locations Table
- In Scanner View have Ownership table selected
- Click on the Place button
- Select the Location from the prompt
- All Ownerships in the table will be associated with the Location placement

### Checkout Ownerships <a name="checkout-ownerships"></a>
To Checkout an Ownership is the action of setting a direct connection between a Borrower and an Ownership.
This would be used to allow another person to borrow or "check out" an item.
For example, if John would like to borrow a hand drill from Mark, Mark would "check out" the Ownership paper towels to borrower "John".

**How to Checkout Ownerships**
- At least one Ownership must be present in the Ownerships Table
- Click on the Checkout button
- If a new Borrower is needed to be created select New
- Enter new Borrower's name and click Create button
- Select Borrower or Self (to checkout item for self use)
- All Ownerships in the table will be associated with the Borrower placement

### Change Quantity <a name="change-quantity"></a>
Changing the quantity of an Ownership helps keep track of inventory and the amount of each item that you have in each Location.

**How to change quantity**
- The Ownership to change must be present in the Ownerships table
- In Scanner View have Ownership table selected
- To increment or decrement the amount value, press the plus or minus button
- As the buttons are pressed the amount will change in real time

---

## Appendix <a name="appendix"></a>
- **Item**: Define item
- **Ownership**: Ownerships are the direct connection between an Item and an Owner.
- **Location**: Define location
- **Borrower**: Define borrower

