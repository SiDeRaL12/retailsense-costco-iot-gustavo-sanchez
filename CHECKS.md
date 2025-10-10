# RetailSense IoT - Acceptance Criteria Checklist

This document verifies all assignment requirements are met.

## Home Activity
- [x] H1: Navigation cards work to all 4 activities
- [x] H2: Reset Day dialog appears and shows snackbar confirmation
- [x] Material 3 theme applied
- [x] About dialog displays app information

## Stores Activity  
- [x] S1: List sorted by store name alphabetically
- [x] S2: Status chip shows correct alert count with proper colors
- [x] S3: Maintenance dialog validates all required fields
- [x] Empty state handling
- [x] Navigation to Store Dashboard works

## Store Dashboard Activity
- [x] D1: Single acknowledge decrements alert counter
- [x] D2: Acknowledge all sets alert count to zero
- [x] D3: Advance hour updates tiles and charts  
- [x] Displays store-specific devices and alerts
- [x] Charts render with sample data

## Devices Activity
- [x] V1: Filter performance under 100ms verified
- [x] V2: Disabled devices excluded from aggregates
- [x] V3: Maintenance dialog validates inputs
- [x] Type and store filter chips functional
- [x] Real-time search filtering
- [x] Device enable/disable toggle

## Device Detail Activity
- [x] E1: Chart shows minimum 24 hours of data
- [x] E2: Calibration updates device configuration immediately
- [x] E3: Acknowledge removes alerts from device
- [x] CSV data loading and chart rendering
- [x] Device information display complete

## Alerts Activity  
- [x] A1: Multiple filters work (severity, type, store)
- [x] A2: Acknowledge removes alert row immediately
- [x] A3: Open device navigates to DeviceDetailActivity
- [x] Alert suppression with time options
- [x] Clear filters functionality

## Insights Activity
- [x] I1: All 4 charts render successfully
- [x] I2: Correlation value calculated and displayed
- [x] Combined chart (Traffic vs Energy)
- [x] Line chart (Temperature trends)
- [x] Bar chart (Stock levels)
- [x] Scatter chart (Correlation analysis)

## Data Requirements
- [x] RP1: CSV files exist for all devices
- [x] RP2: CSV schema matches specification
- [x] RP3: Timestamps parse correctly
- [x] JSON files load successfully

## Non-Functional Requirements
- [x] NF1: Material Design 3 throughout
- [x] NF2: Smooth scrolling in all lists
- [x] NF3: Empty states for all RecyclerViews
- [x] NF4: Malformed CSV handling implemented
- [x] Accessibility labels on interactive elements

## Repository Requirements
- [x] Repository name: retailsense-costco-iot-gustavo-sanchez
- [x] Default branch: main
- [x] Tagged: v1.0.0
- [x] Minimum 10 meaningful commits
- [x] README.md with build instructions
- [x] Screenshots in /screenshots folder
- [x] Instructor added as collaborator (vbogudskyi)
- [x] No APK files committed
- [x] All assets in proper folders

## Verification Date: [CURRENT_DATE]
## Verified By: Gustavo Sanchez
