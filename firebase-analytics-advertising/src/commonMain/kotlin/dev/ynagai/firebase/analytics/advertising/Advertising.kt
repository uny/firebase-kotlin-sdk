/**
 * Advertising-ID (IDFA / AAID) collection for `firebase-analytics`.
 *
 * This module has no API. Adding it as a dependency alongside `firebase-analytics`
 * links the advertising-ID-collecting variants on each platform:
 *
 * - **iOS**: links the `FirebaseAnalyticsIdentitySupport` SPM product (enables IDFA).
 * - **Android**: re-introduces `play-services-ads-identifier` (enables AAID collection).
 *
 * By default `firebase-analytics` does NOT collect the advertising ID. Depend on this
 * module only if you need advertising attribution (e.g. Google Ads, remarketing
 * audiences), and update your App Store / Play Data safety privacy declarations
 * accordingly.
 */
package dev.ynagai.firebase.analytics.advertising
