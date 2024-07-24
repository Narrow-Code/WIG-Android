# WIG - Android

## Table of Contents
1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
   - [Android Installation](#android-installation)
   - [Self-Hosting Setup](#self-host-setup)
   - [Sign-Up](#sign-up)
   - [Login](#login)
3. [Scanner View](#scanner-view)
   1. [Ownership Table Features](#ownership-table-features)
      - [Add New Ownership](#add-new-ownership)
      - [Edit Ownership](#edit-ownership)
      - [Remove Ownership](#remove-ownership)
      - [Clear Ownership](#clear-ownerships)
      - [Search Ownerships](#search-ownerships)
      - [Place Ownerships](#place-ownerships)
      - [Checkout Ownership](#checkout-ownerships)
      - [Change Quantity](#change-quantity)
   2. [Location Table Features](#locations-table-features)
      - [Add New Location](#add-new-location)
      - [Edit Location](#edit-location)
      - [Remove Location](#remove-location)
      - [Clear Location](#clear-locations)
      - [Search Locations](#search-locations)
      - [Unpack Location](#unpack-locations)
4. [Checked-Out View](#checked-out-view)
   - [Expand/Collapse a Borrower](#expand-collapse-a-borrower)
   - [Return Single Ownership](#return-single-ownership)
   - [Return All From Single Borrower](#return-all-from-single-borrower)
   - [Return All](#return-all)
5. [Inventory View](#inventory-view)
   - [Expand/Collapse a Location](#expand-collapse-a-location)
   - [Delete Ownership](#delete-ownership)
6. [Settings](#settings)
   - [Vibrate On Scan](#vibrate-on-scan)
   - [Sound On Scan](#sound-on-scan)
   - [App Opens To](#app-opens-to)
   - [Logout](#logout)
7. [Appendix](#appendix)

---

## Introduction <a name="introduction"></a>
- WIG is a self-hosted inventory management service to remember "What I Got".
- It is built for the everyday person in need of organization.
- Utilizing barcode and QR scanning, its UI/UX enables easy access to ownership items and locations.
- WIG gives the ability to store items into locations, to find items easily via the mobile app.
- Item inventory tracking makes checking supply stock flawless from a distance.

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

## Scanner View
Scanner view can be accessed by clicking on the QR icon in the top right hand navigation bar.

The Scanner View is where most of the inventory management will happen, it allows scanning QR and barcodes to manage inventory.

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
- Confirm the Ownership removal

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

## Location Table Features <a name="location-table-features"></a>
Location Table is able to be accessed via the Scanner View page, then selecting the Locations button above the table.

### Add New Location <a name="add-new-location"></a>
There are two ways to add a Location to the inventory.
   1. **Scan Unused QR Code**
      - In Scanner View, use the camera and aim it at a QR code
      - The Scanner will auto detect and send to the server
      - If the QR exists as a Location or Ownership it will return existing Location or Ownership
      - If the QR does not exist in database it will prompt open the Manually Add option

   2. **Manually Add**
      - In Scanner View, click on the Plus + icon on the top right of the Location table
      - Enter the Locations's Name and a QR-Code to associate with it
      - Select **Location** from the dropdown menu
      - Click **Create*
      - The Location will then be added to your inventory and can be scanned for later use
 
### Edit Location <a name="edit-location"></a>
An Location can be edited to change to following fields:
- Name: This is the display name to appear on any table
- QR: This is the customized code that can be scanned to retrieve the Location
- Tags: A keyword or term assigned for search criteria
- Notes/Description: Customized notes as self reminders about the Location

**How to Edit**
- In Scanner View have Locations table selected
- Have Location added to table
- Click on the Location you wish to edit
- Fill out the fields to edit
- Click Save

### Remove Location <a name="remove-location"></a>
Removing a Location only removes it from the Location table. It does not Delete it from inventory.

**How to Remove Single Location**
- In Scanner View have Locations table selected
- Long hold click on the Location to remove
- Confirm the Location removal

### Clear Location <a name="clear-locations"></a>
Clearing Locations removes all Locations from the Locations table. It does not Delete any from the inventory.

**How to Clear Locations**
- In Scanner View have Locations table selected
- Click the **CLEAR** button toward the bottom left of the table

### Search Locations <a name="search-locations"></a>
- In Scanner View have Locations table selected
- Click on magnifying glass icon toward upper right hand side of Locations table
- Enter search criteria and select Search
- Click on the Location you would like to add to the table
- Click the Add button in the prompt
- Click Cancel button at the bottom of the search popup
- Locations will be added to the table

### Unpack Locations <a name="unpack-locations"></a>
Unpacking Locations is the action to populate the Ownerships table with all Ownerships that are associated with any Location inside of the Locations table.

For example, John has 2 rolls of paper towels and a wrench in his grey bin, he also has a screw driver in his purple box.
John has his purple box and his grey bin both populated in his Locations Table.
When John clicks on **Unpack** his Ownership table populates with his paper towels, wrench and screw driver.

**How to Unpack Locations**
- In Scanner View have Locations Table selected
- Must have at least one Location populated in the table
- Click on the Unpack button
- Ownership Table will then be populated
- Navigate to Ownerships Table to view all Ownerships

## Checked Out View <a name="checked-out-view"></a>
The Checked Out View can be accessed by clicking on the giving hand icon in the top right navigation bar.

The Checked Out View is a table to help easily manage and see all Checked Out items.

### Expand/Collapse a Borrower <a name="expand-collapse-a-borrower"></a>
You may Expand or Collapse a borrower to view what has been borrowed by the individual borrower.

- Click on collapsed borrower to expand
- Click on expanded borrower to collapse

### Return Single Ownership <a name="return-single-ownership"></a>
Returning a single ownership removes the Chcked Out status and returns it to it's original location.

- Long hold click on an Ownership
- Click Return button

### Return All From Single Borrower <a name="return-all-from-single-borrower"></a>
Returning all from a single Borrower removes the Checked Out status from all Ownerships checked out to an individual borrower and returns them to their original Locations.

- Long hold click on a single Borrower
- Click Return button

### Return All <a name="return-all"></a>
Return All removes the Checked Out status from all Ownerships checked out to all borrowers and returns them to their original Locations.

- Click on the Return All button at the bottom of the table
- Click Return button

## Inventory View <a name="inventory-view"></a>
Inventory View can be accessed by clicking on the filing cabinet icon in the navigation bar located on the top right hand corner of the app.

Inventory View shows an expandable list of the users entire inventory

### Expand/Collapse a Location<a name="expand-collapse-a-location"></a>
You may Expand or Collapse a location to view what has been placed inside of the location.

- Click on the collapsed Location to expand
- Click on the epxanded Location to collapse

### Delete Ownership <a name="delete-ownership"></a>
Deleting an Ownership removes it completely fom the database.

- Expand the Location that is holding the Ownership
- Long hold click on the Ownership
- Click on the Delete button

## Settings <a name="settings"></a>
The Settings View may be accessed by clicking on the gear icon in the navigation bar located at the top right hand corner of the app.

There are a few settings to change the way the app functions.

### Vibrate On Scan <a name="vibrate-on-scan"></a>
If vibrate on Scan is selected, everytime a barcode or QR is scanned, the users phone will vibrate indicating the scan.

### Sound On Scan <a name="sound-on-scan"></a>
If sound on scan is selected, every time a barcode or QR is scanned, the users phone will make a notification sound indicating the scan.

### App Opens To <a name="app-opens-to"></a>
There are two options for the app to open to. Which ever one is selected, the app will always start up on the selected page.

- [Scanner View](#scanner-view)
- [Inventory View](#inventory-view)

### Logout <a name="logout"></a>
The user may select the Logout button to successfully log out of the application.

## Appendix <a name="appendix"></a>
- **Owner**: The Owner represents the user of the application.
- **Item**: An Item represents a physical item that is owned by the Owner.
- **Ownership**: Ownerships are the direct connection between an Item, Owner and Location.
- **Location**: A Location represents a physical location which can store Items inside of. Examples, bins, boxes, containers, closets, rooms, etc.
- **Borrower**: A Borrower represents a person that may check out or "borrower" items.
