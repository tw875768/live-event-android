# Live Event Android

A sample Android app using Twitter Kit to help participants follow what's happening at an event.

![Screens from the app](screenshot.png)

(originally written by Luis Cipriani as a sample from Qcon Sao Paulo)

## Features

* Event hashtag timeline
* Create a Tweet with event hashtag pre-filled
* Follow other participants by scanning a QR code (in the app or printed on conference badge)
* Share your user handle QR Code to someone
* Follow event organizer accounts

## Twitter Kit features

This app uses the following Twitter Kit features:

 * native Tweet Composer
 * Sign in with Twitter
 * Search Timeline
 * Twitter API calls
 * Twitter API client extension

## Building

If you want to run the app locally, do the following:

1. Import the project in your IDE (we use gradle to build)
2. Create a Twitter app at apps.twitter.com
3. Rename `app/twitter.sample.properties` to `app/twitter.properties`
4. Fill in your keys there
5. Customize the `strings.xml` file with your event hashtags and search criteria
5. Run it

See [how to integrate the SDK](https://dev.twitter.com/twitterkit/android/installation) if you need more help.

## Get support

If you need support to build the app or to understand any part of the code, let us know. Post your question in the [Twitter Developer Community](https://twittercommunity.com/c/publisher/twitter) forum.

## Contributing

The goal of this project is to be an example for Twitter Kit and we strive to keep it simple. We definitely welcome improvements and fixes, but we may not merge every pull request suggested by the community.

The rules for contributing are available in the `CONTRIBUTING.md` file.

## Contributors

* [Luis Cipriani](https://twitter.com/lfcipriani)
* [Andy Piper](https://twitter.com/andypiper)

## License

Copyright 2015 Twitter, Inc and other contributors.

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
