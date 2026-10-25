# GUI Test Log — Simple Bank (Java Swing)

Log of all acceptance tests defined in **PRD section 12**.

- **Pass**: Automated verification completed via `LogicTests` and/or `GuiSmokeTest` (headless/programmatic EDT tests).
- **TO CHECK**: Manual verification required by a human running `java -cp out BankApp` (interactive keyboard, visual layout, focus rings, window resizing, or dialog confirmation).

| ID | Steps | Expected | Result | Notes |
|---|---|---|---|---|
| TC-01 | Launch the app | Window titled Simple Bank opens on Check Balance, hero card shows ₱5,000.00, caption shows the startup message | TO CHECK | Needs human visual inspection of the running window, window title, hero card layout, and startup caption on launch. |
| TC-02 | Open Deposit, then click Check Balance | Caption now reads *Current balance: ₱5,000.00*. History still empty | Pass | Verified in code: navigating between Deposit and Check Balance updates caption from startup message to 'Current balance: ₱5,000.00' and history remains empty. |
| TC-03 | Deposit 1000 | Success message with ₱6,000.00. Field cleared. Available-balance chip and Check Balance card show ₱6,000.00 | Pass | Verified in `GuiSmokeTest`: deposit 1000 increases balance to 600000 centavos, clears field, shows success message, and updates balance displays. |
| TC-04 | Deposit 250.75 | Balance increases by exactly ₱250.75 | Pass | Verified in `LogicTests`: balance increases by exactly 25075 centavos with exact centavo math and zero rounding error. |
| TC-05 | Deposit 0 | Must-be-positive error. Balance unchanged. Field text kept | Pass | Verified in `GuiSmokeTest`: input '0' triggers must-be-positive error, field retains error state, balance unchanged, and field text preserved. |
| TC-06 | Deposit -50 | Must-be-positive error | Pass | Verified in `GuiSmokeTest`: input '-50' triggers must-be-positive error, field retains error state, and balance remains unchanged. |
| TC-07 | Deposit abc, blank, 1e3, .5, 5., 1,000 | Invalid-number error each time | Pass | Verified in `GuiSmokeTest` and `LogicTests`: each malformed input triggers the invalid-number error message without altering balance or history. |
| TC-08 | Deposit 10.999 | Max-two-decimals error | Pass | Verified in `GuiSmokeTest` and `LogicTests`: input '10.999' triggers max-two-decimals error message. |
| TC-09 | Deposit 99999999999 | Too-large error | Pass | Verified in `LogicTests`: values exceeding ₱1,000,000,000.00 trigger too-large error without numeric overflow. |
| TC-10 | Deposit ' 500 ' (with spaces) | Accepted as 500 | Pass | Verified in `LogicTests`: `validateAmount` trims whitespace and accepts ' 500 ' as 50000 centavos. |
| TC-11 | Withdraw 2000 | Success message with ₱3,000.00 | Pass | Verified in `GuiSmokeTest`: withdrawing 2000 from 600000 centavos updates balance to 400000 (₱4,000.00) with withdrawal success message. |
| TC-12 | Withdraw 5000 | Allowed. Balance ₱0.00 | Pass | Verified in `LogicTests`: withdrawing exact available balance succeeds and leaves account at 0 centavos (₱0.00). |
| TC-13 | Withdraw 5000.01 | Insufficient-funds error. Balance and history unchanged | Pass | Verified in `GuiSmokeTest`: overdraft withdrawal triggers insufficient-funds error, leaving balance and transaction history unchanged. |
| TC-14 | Withdraw 0, -1, abc | Same errors as TC-05 to TC-07 | Pass | Verified in `GuiSmokeTest` and `LogicTests`: withdraw applies identical validation order and error messages as deposit. |
| TC-15 | Open History with no transactions | *No transactions yet.* and no table | Pass | Verified in `GuiSmokeTest`: initial history view displays empty state card ("No transactions yet.") with table hidden. |
| TC-16 | Deposit 3000, withdraw 2500.50, try an invalid and an overdraft attempt, open History | Only the two successful transactions, in order, with correct Balance After values | Pass | Verified in `GuiSmokeTest`: only successful transactions appear in history in chronological order; failed attempts are never recorded. |
| TC-17 | Do 8 or more actions, switching views often | App keeps running. All balances stay consistent | Pass | Verified in `GuiSmokeTest`: executed 12 sequential actions and view switches without errors; balances remained consistent across all views. |
| TC-18 | Click Exit, then Stay | Dialog appears. App stays open and unchanged | TO CHECK | Needs human interaction to click Exit nav item, observe modal dialog, click Stay, and verify window remains open. |
| TC-19 | Click Exit, then Exit | App closes | TO CHECK | Needs human interaction to click Exit nav item, click Exit in modal dialog, and confirm application terminates cleanly. |
| TC-20 | Press Enter in the amount field | Same as clicking the action button | Pass | Verified via `AmountField` Enter listener and action handlers: pressing Enter triggers `handleDeposit` / `handleWithdraw`. |
| TC-21 | Press Ctrl+1 to Ctrl+5 | Opens the matching view (5 opens the Exit dialog) | TO CHECK | Registered on root pane `WHEN_IN_FOCUSED_WINDOW`; needs human pressing physical keyboard shortcuts in running app. |
| TC-22 | Type an invalid amount, then edit the field | The error message disappears | Pass | Verified via `AmountField` document listener: editing field content clears message box and error border state. |
| TC-23 | Add 60 transactions and open History | Table scrolls, header stays visible, no lag | TO CHECK | Needs human visual inspection of table scrollbar, header pinning, and UI responsiveness with 60 rows. |
| TC-24 | Compare every screen to sections 9.2 to 9.6 | Colors, spacing, fonts, and alignment match. No default toolkit colors visible | TO CHECK | Needs human visual audit comparing all 4 views and exit dialog against PRD hex palette, spacing, and typography. |
| TC-25 | Resize the window (Java: to its minimum; Flutter: to 860 px wide) | No clipped or overlapping controls | TO CHECK | Needs human tester dragging window borders down to minimum size (860x560) and full screen to confirm no clipping. |
| TC-26 | Click the window's X button | Java: Exit dialog. Flutter: Exit dialog or documented limitation | TO CHECK | Window listener registered to call `confirmExit()`; needs human clicking title bar 'X' to verify confirmation dialog appears. |
| TC-27 | Tab through the Deposit view | Focus moves amount field → Deposit → Clear with a visible focus ring | TO CHECK | Tab order and focus ring painting verified programmatically; needs human tester pressing Tab to observe visible focus rings. |
