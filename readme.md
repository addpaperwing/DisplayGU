## Repo Quick Look
A toy app built with Kotlin and MVVM architecture, using GitHub [Search Repositories API](https://docs.github.com/en/rest/search?apiVersion=2022-11-28#search-repositories). Still working on it and trying to add more features.
#### Tech Stack, Libraries
1. Kotlin base
2. Coroutine + Flow + LiveData 
3. Jetpack 
	 - Lifecycle + ViewModel 
	 - ViewBinding
	 - Hilt
4. Retrofit2 + Moshi 
5. Junit + Hamcrest + Mockk + Espresso
#### Try it
[APK](https://github.com/addpaperwing/Repo-QuickLook/releases/tag/release)
#### TODO
1. Paging
2. Authentication system for [Rate limit](https://docs.github.com/en/rest/search?apiVersion=2022-11-28#rate-limit)
3. Shows readme natively instead of a webview
4. Playstore
...