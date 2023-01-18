package dev.bitspittle.firebase.auth

import dev.bitspittle.firebase.util.FirebaseError

class AuthError internal constructor(
    private val wrapped: dev.bitspittle.firebase.externals.auth.AuthError
) : Exception(), FirebaseError<AuthError.Code> {
    // From https://firebase.google.com/docs/reference/js/auth
    enum class Code(override val text: String) : FirebaseError.Code {
        AdminOnlyOperation("auth/admin-restricted-operation"),
        AlreadyInitialized("auth/already-initialized"),
        ArgumentError("auth/argument-error"),
        AppNotAuthorized("auth/app-not-authorized"),
        AppNotInstalled("auth/app-not-installed"),
        CaptchaCheckFailed("auth/captcha-check-failed"),
        CodeExpired("auth/code-expired"),
        CordovaNotReady("auth/cordova-not-ready"),
        CorsUnsupported("auth/cors-unsupported"),
        CredentialAlreadyInUse("auth/credential-already-in-use"),
        CredentialMismatch("auth/custom-token-mismatch"),
        CredentialTooOldLoginAgain("auth/requires-recent-login"),
        DependentSdkInitBeforeAuth("auth/dependent-sdk-initialized-before-auth"),
        DynamicLinkNotActivated("auth/dynamic-link-not-activated"),
        EmailChangeNeedsVerification("auth/email-change-needs-verification"),
        EmailExists("auth/email-already-in-use"),
        EmulatorConfigFailed("auth/emulator-config-failed"),
        ExpiredOobCode("auth/expired-action-code"),
        ExpiredPopupRequest("auth/cancelled-popup-request"),
        InternalError("auth/internal-error"),
        InvalidApiKey("auth/invalid-api-key"),
        InvalidAppCredential("auth/invalid-app-credential"),
        InvalidAppId("auth/invalid-app-id"),
        InvalidAuth("auth/invalid-user-token"),
        InvalidAuthEvent("auth/invalid-auth-event"),
        InvalidCertHash("auth/invalid-cert-hash"),
        InvalidCode("auth/invalid-verification-code"),
        InvalidContinueUri("auth/invalid-continue-uri"),
        InvalidCordovaConfiguration("auth/invalid-cordova-configuration"),
        InvalidCustomToken("auth/invalid-custom-token"),
        InvalidDynamicLinkDomain("auth/invalid-dynamic-link-domain"),
        InvalidEmail("auth/invalid-email"),
        InvalidEmulatorScheme("auth/invalid-emulator-scheme"),
        InvalidIdpResponse("auth/invalid-credential"),
        InvalidMessagePayload("auth/invalid-message-payload"),
        InvalidMfaSession("auth/invalid-multi-factor-session"),
        InvalidOauthClientId("auth/invalid-oauth-client-id"),
        InvalidOauthProvider("auth/invalid-oauth-provider"),
        InvalidOobCode("auth/invalid-action-code"),
        InvalidOrigin("auth/unauthorized-domain"),
        InvalidPassword("auth/wrong-password"),
        InvalidPersistence("auth/invalid-persistence-type"),
        InvalidPhoneNumber("auth/invalid-phone-number"),
        InvalidProviderId("auth/invalid-provider-id"),
        InvalidRecipientEmail("auth/invalid-recipient-email"),
        InvalidSender("auth/invalid-sender"),
        InvalidSessionInfo("auth/invalid-verification-id"),
        InvalidTenantId("auth/invalid-tenant-id"),
        MfaInfoNotFound("auth/multi-factor-info-not-found"),
        MfaRequired("auth/multi-factor-auth-required"),
        MissingAndroidPackageName("auth/missing-android-pkg-name"),
        MissingAppCredential("auth/missing-app-credential"),
        MissingAuthDomain("auth/auth-domain-config-required"),
        MissingCode("auth/missing-verification-code"),
        MissingContinueUri("auth/missing-continue-uri"),
        MissingIframeStart("auth/missing-iframe-start"),
        MissingIosBundleId("auth/missing-ios-bundle-id"),
        MissingOrInvalidNonce("auth/missing-or-invalid-nonce"),
        MissingMfaInfo("auth/missing-multi-factor-info"),
        MissingMfaSession("auth/missing-multi-factor-session"),
        MissingPhoneNumber("auth/missing-phone-number"),
        MissingSessionInfo("auth/missing-verification-id"),
        ModuleDestroyed("auth/app-deleted"),
        NeedConfirmation("auth/account-exists-with-different-credential"),
        NetworkRequestFailed("auth/network-request-failed"),
        NullUser("auth/null-user"),
        NoAuthEvent("auth/no-auth-event"),
        NoSuchProvider("auth/no-such-provider"),
        OperationNotAllowed("auth/operation-not-allowed"),
        OperationNotSupported("auth/operation-not-supported-in-this-environment"),
        PopupBlocked("auth/popup-blocked"),
        PopupClosedByUser("auth/popup-closed-by-user"),
        ProviderAlreadyLinked("auth/provider-already-linked"),
        QuotaExceeded("auth/quota-exceeded"),
        RedirectCancelledByUser("auth/redirect-cancelled-by-user"),
        RedirectOperationPending("auth/redirect-operation-pending"),
        RejectedCredential("auth/rejected-credential"),
        SecondFactorAlreadyEnrolled("auth/second-factor-already-in-use"),
        SecondFactorLimitExceeded("auth/maximum-second-factor-count-exceeded"),
        TenantIdMismatch("auth/tenant-id-mismatch"),
        Timeout("auth/timeout"),
        TokenExpired("auth/user-token-expired"),
        TooManyAttemptsTryLater("auth/too-many-requests"),
        UnauthorizedDomain("auth/unauthorized-continue-uri"),
        UnsupportedFirstFactor("auth/unsupported-first-factor"),
        UnsupportedPersistence("auth/unsupported-persistence-type"),
        UnsupportedTenantOperation("auth/unsupported-tenant-operation"),
        UnverifiedEmail("auth/unverified-email"),
        UserCancelled("auth/user-cancelled"),
        UserDeleted("auth/user-not-found"),
        UserDisabled("auth/user-disabled"),
        UserMismatch("auth/user-mismatch"),
        UserSignedOut("auth/user-signed-out"),
        WeakPassword("auth/weak-password"),
        WebStorageUnsupported("auth/web-storage-unsupported");

        companion object {
            fun from(code: String): Code {
                return values().first { it.text == code }
            }
        }
    }


    val customData get() = wrapped.customData
    override val code get() = Code.from(wrapped.code)
    override val message get() = wrapped.message
}
