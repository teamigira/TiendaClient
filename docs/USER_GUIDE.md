# Tienda POS user guide

Version 2.1 · For shop owners and cashiers

> Screenshots are marked **[Screenshot: …]**. Take them from the installed app
> before you print or publish this guide.

## 1. Install

1. Run **Tienda POS-2.1.0.msi** and follow the steps. You can choose the
   folder; the default is fine.
2. Open **Tienda POS** from the Start menu or the desktop shortcut.

Tienda keeps its data in `%LOCALAPPDATA%\Tienda` (for example
`C:\Users\Asha\AppData\Local\Tienda`). Uninstalling the program does not delete
this folder, so your sales are safe if you reinstall.

## 2. Activate

The first time Tienda opens it shows **Activate Tienda POS**.
**[Screenshot: activation window]**

- **Free trial:** click **Continue trial** to use the Standard edition for 14
  days.
- **Buy a licence:**
  1. Click **Copy** next to *this computer's code* and send the code to your
     Tienda seller (WhatsApp, SMS or email).
  2. The seller sends back a licence key starting with `TIENDA1.`.
  3. Paste it in the box and click **Activate**.

The licence works on that computer only. To move Tienda to a new computer, send
the new computer's code and ask for a new key.

**Renewing:** click the licence line at the bottom of the main window (for
example *Premium until 23-Nov-2027*) to open the same window and paste a new key.

## 3. First login

- User name: **admin** · Password: **12345**
- Tienda then asks you to choose a new password (at least 8 characters). Keep
  it safe: nobody, including the seller, can read it back.

After five wrong passwords the login screen waits 30 seconds.

## 4. Staff accounts

**Administrator (F6) → Users → New User.** Give each cashier their own account
and a role. Only administrators can restore backups or change roles.
**[Screenshot: new user form]**

## 5. Products and stock

- **New Product:** code, name, brand, category, cost, sale price, stock level
  and the minimum stock level (Tienda warns you when stock falls to it).
- **Barcodes:** put the product's barcode in **EAN/GTIN**. On the sale screen,
  scanning that barcode adds the product.
- **Low stock:** the warning at the top of the main window shows how many
  products are running low; click it for the list.
**[Screenshot: product form]**

## 6. Selling

1. **New Sale - POS (F3).**
2. Scan a barcode, or type a product code or name and pick it from the list,
   then press **Enter**. The quantity defaults to 1.
3. Repeat for each item, then **Complete order (save)**.
4. The receipt preview opens; click **Print**.

Other order actions (menu **Order**): hold the current order and serve the next
customer, reopen a held order, or reopen a completed order to correct it.
**[Screenshot: sale screen with items]**

## 7. Receipts and printer

- **Printer Settings:** choose the printer, your company name, address and
  footer message, then a receipt design (101 to 105). The preview on the right
  shows exactly what will print. Design 105 (Signature) needs Premium.
- **Tools → Receipt printer:** 80 mm or 58 mm paper, and whether to open the
  cash drawer after each receipt. Use **Test cash drawer** to check it.
**[Screenshot: printer settings with preview]**

## 8. Backups (important)

- Tienda makes a backup every day when it starts and keeps the last 7.
- **Tools → Second backup folder:** pick a USB flash drive or a OneDrive /
  Google Drive folder. Each daily backup is copied there too, so a broken
  computer does not lose your records. **Do this on day one.**
- **Tools → Open backups folder** shows the backup files.
- **Tools → Restore database** (administrators only) replaces the current data
  with a backup. Tienda saves the current data first
  (`Tienda_before_restore_…zip`), then closes; open it again afterwards.

## 9. Language / Lugha

**Tools → Language / Lugha → Kiswahili**, then close and reopen Tienda.

## 10. Reports and Premium features

- **Reports:** sales by day and week, best-selling products.
- **Premium ★ menu:** CSV export of products and sales, manual backup to any
  file, best-seller analytics, customer loyalty points, expiry alerts, a second
  currency on receipts, and receipt design 105.

## 11. Problems

| Problem | What to do |
| --- | --- |
| "This licence is for another computer" | The key was made for a different code. Send the code shown and ask for a new key. |
| "This licence expired" | Ask your seller for a renewal key. |
| "This computer's date is earlier than…" | Set the correct date and time in Windows. |
| Scanned barcode not found | Put the barcode in the product's EAN/GTIN field. |
| Printer prints nothing | Check the printer in Printer Settings, and the paper width in Tools → Receipt printer. |
| Anything else | Send your seller the files in `%LOCALAPPDATA%\Tienda\logs`. They show what went wrong. |
