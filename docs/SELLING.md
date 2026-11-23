# Selling Tienda POS: the seller's handbook

For you, the seller. Not for customers.

## 1. Issuing licences

The private signing key is in `~/.tienda-licensing/private.key` on the
computer where it was created. **Back it up now** to two places that are
offline and private (an encrypted USB drive, a password manager). If it is
lost you cannot issue or renew licences; if it leaks, anyone can make keys,
and you would have to release a new app version with a new key.

```bash
# the customer sends you their computer code, e.g. YNWV5-P4RE7-M798Q-26CQM
java tools/licensing/TiendaLicenseTool.java issue \
  --customer "Duka la Mama Asha, Kariakoo" --tier premium \
  --machine YNWV5-P4RE7-M798Q-26CQM --months 12
```

- `--tier standard|premium`, `--months N` or `--until 2027-12-31`.
- `--machine ANY` makes a key that works on any computer. Use it only for
  your own test machines: it can be shared freely.
- Every key issued is logged in `~/.tienda-licensing/issued.csv`, which is your
  customer list for renewals.
- Renewal = issue a new key for the same code; the customer pastes it via the
  licence line in the main window.

Customers on the old monthly 16-digit codes need a new key after updating
to 2.1.

## 2. Suggested pricing (adjust to your market)

| | Standard | Premium |
| --- | --- | --- |
| Yearly licence, per computer | TZS 240,000 | TZS 420,000 |
| Monthly option | TZS 25,000 | TZS 45,000 |
| Installation and training visit | TZS 50,000 | included |

Reasoning: a yearly price below the cost of a basic receipt printer is an
easy decision for a small shop, and monthly keys suit cash-flow-conscious
shops. Premium's extras are things owners notice: receipt design 105, loyalty,
analytics, CSV export, expiry alerts, a second currency. Offer a discount for
a second computer in the same shop (same key flow, one key per computer).

The 14-day trial is built in, so demos need no key.

## 3. Support

- Channels: WhatsApp number and email on the receipt footer and in the EULA.
- First questions to ask: Tienda version (bottom of the main window), the
  licence line, and the log files from `%LOCALAPPDATA%\Tienda\logs`.
- Data recovery: backups are in `%LOCALAPPDATA%\Tienda\backups` and the second
  folder the shop chose. Tools → Restore database.
- Suggested promise: reply within one working day; remote help for setup
  problems; a fix release for real bugs.

## 4. Releasing a new version

1. Raise the version in `pom.xml` and `AppInfo.VERSION` (a test checks they
   match).
2. `mvn test`: all tests must pass.
3. On Windows: `powershell -ExecutionPolicy Bypass -File packaging\build-installer.ps1`
   (needs JDK 21, Maven, WiX 3.14). Set `SIGN_PFX` / `SIGN_PASSWORD` to sign.
4. Install it over the previous version on a test PC and check sales,
   printing and that old data is still there.
5. Upload the .msi and update the JSON at your `update.url`, for example:
   `{"version":"2.2.0","url":"https://…/TiendaPOS-2.2.0.msi","notes":"…"}`.

## 5. Before the first sale: checklist

- [ ] Back up `~/.tienda-licensing/private.key` (twice, offline).
- [ ] Code-signing certificate (otherwise Windows SmartScreen warns
      "unknown publisher"). An OV certificate costs roughly USD 200–400/year.
- [ ] Icons8: buy the licence, or show "Icons by Icons8" with a link in the app.
- [ ] Lawyer review of `packaging/EULA.txt` and `docs/PRIVACY.md`; fill in your
      business details.
- [ ] Native-speaker review of `src/main/resources/i18n/sw.tsv`.
- [ ] Test on the receipt printers and scanners your customers buy (58 and
      80 mm, cash drawer).
- [ ] Screenshots for `docs/USER_GUIDE.md`.
- [ ] Decide on update hosting and set `update.url` in
      `src/main/resources/application.properties`.
- [ ] Decide on several tills in one shop (below).

## 6. Several tills in one shop (decision needed)

Today each computer has its own database. Options:

1. **One computer per shop** (current). Simplest; sell a licence per shop.
2. **Shared H2 database** (H2's server mode, one PC hosts the data): small
   change, works on a local network, the host PC must be on.
3. **Central MySQL/PostgreSQL server:** most robust for chains, needs a
   database server and more setup work.

Recommendation: sell single-till first; build option 2 when a customer asks
for two tills, since it reuses everything that exists.

## 7. Not done yet (by your choice)

- TRA fiscal receipts (EFD/VFD integration) and the VAT rate on receipts,
  waiting on your confirmation.
- Demo video.
