# Health monitor

Android application for monitoring services statuses.


## Build

To build health monitor application you should perform several actions:
- Add path to your `google-services.json` and your application id
(the one you have in your firebase project) in `gradle.properties`.

```properties
google_services_path = /path/to/google-services.json
application_id = firebase.application.id
```
- Add firebase topic to `src/main/res/values/strings.xml`.
All clients will be subscribed to that topic.

```xml
<resources>
    ...
    <string name="firebase_topic">firebase.topic</string>
    ...
</resources>
```

And then run

```bash
./gradlew build
```

Generated `.apk` files can be found under `build/outputs/apk/` folder. There are
signed debug file and unsigned release file respectively. Most likely you are looking
for `build/outputs/apk/debug/android-debug.apk`.