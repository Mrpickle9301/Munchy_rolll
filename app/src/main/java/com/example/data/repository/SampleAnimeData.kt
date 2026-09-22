package com.example.data.repository

import com.example.data.model.Anime
import com.example.data.model.AnimeCharacter
import com.example.data.model.Episode

object SampleAnimeData {

    val curatedAnimeList: List<Anime> = listOf(
        Anime(
            id = 52991,
            title = "Solo Leveling: Arise",
            titleJapanese = "俺だけレベルアップな件",
            synopsis = "In a world where hunters—humans who possess magical powers—must battle deadly monsters to protect humanity, Sung Jinwoo, notoriously known as the weakest hunter of all mankind, finds himself in a fatal dual dungeon. Given a second chance at life through a mysterious quest log visible only to him, Jinwoo discovers he has the unique ability to level up infinitely.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1160/141546l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&q=80",
            score = 8.52,
            scoredBy = 340000,
            rank = 84,
            popularity = 45,
            episodes = 12,
            status = "Finished Airing",
            rating = "R - 17+",
            season = "Winter",
            year = 2024,
            genres = listOf("Action", "Fantasy", "Supernatural"),
            trailerYoutubeId = "oT_4zfvUq8k",
            studio = "A-1 Pictures"
        ),
        Anime(
            id = 52992,
            title = "Frieren: Beyond Journey's End",
            titleJapanese = "葬送のフリーレン",
            synopsis = "The demon king has been defeated, and the victorious hero party returns home before disbanding. The four—mage Frieren, hero Himmel, priest Heiter, and warrior Eisen—reminisce about their decade-long journey. But the passing of time is different for elves. Frieren witnesses her companions slowly pass away, leading her on a poignant new quest to understand the ephemeral hearts of humans.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&q=80",
            score = 9.34,
            scoredBy = 480000,
            rank = 1,
            popularity = 38,
            episodes = 28,
            status = "Finished Airing",
            rating = "PG-13",
            season = "Fall",
            year = 2023,
            genres = listOf("Adventure", "Drama", "Fantasy"),
            trailerYoutubeId = "qgQunxD0qMo",
            studio = "Madhouse"
        ),
        Anime(
            id = 40748,
            title = "Jujutsu Kaisen: Shibuya Incident",
            titleJapanese = "呪術廻戦",
            synopsis = "October 31st. A curtain falls over Shibuya, trapping countless civilians. To rescue them, Satoru Gojo descends into the subway depths alone, stepping directly into an intricate trap laid by Suguru Geto and disaster curses. Yuji Itadori and Jujutsu High sorcerers rush into an unprecedented battle that will reshape the jujutsu world forever.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1792/138022l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=1200&q=80",
            score = 8.84,
            scoredBy = 720000,
            rank = 28,
            popularity = 12,
            episodes = 23,
            status = "Finished Airing",
            rating = "R - 17+",
            season = "Summer",
            year = 2023,
            genres = listOf("Action", "Dark Fantasy", "Supernatural"),
            trailerYoutubeId = "O6qVieflwQs",
            studio = "MAPPA"
        ),
        Anime(
            id = 38000,
            title = "Demon Slayer: Kimetsu no Yaiba - Hashira Training Arc",
            titleJapanese = "鬼滅の刃 柱稽古編",
            synopsis = "Tanjiro visits the Stone Hashira, Himejima, who intends to prepare him for the battles to come. The training to become a Hashira is rigorous and demanding, and earning Himejima's approval seems impossible, but Tanjiro won't give up! Meanwhile, Muzan continues his search for Nezuko and Ubuyashiki.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1565/141753l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&q=80",
            score = 8.35,
            scoredBy = 280000,
            rank = 112,
            popularity = 22,
            episodes = 8,
            status = "Finished Airing",
            rating = "R - 17+",
            season = "Spring",
            year = 2024,
            genres = listOf("Action", "Historical", "Supernatural"),
            trailerYoutubeId = "0q5E5p0w3uU",
            studio = "ufotable"
        ),
        Anime(
            id = 44511,
            title = "Chainsaw Man",
            titleJapanese = "チェンソーマン",
            synopsis = "Denji is a young man who will do anything for money, even hunting down devils with his pet devil Pochita. He's a simple man with simple dreams, drowning under a mountain of debt. But his sad life gets turned upside down one day when he's betrayed by someone he trusts. Now with the power of a devil inside him, Denji's become a whole new man—Chainsaw Man!",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1806/126216l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=1200&q=80",
            score = 8.49,
            scoredBy = 650000,
            rank = 95,
            popularity = 18,
            episodes = 12,
            status = "Finished Airing",
            rating = "R - 17+",
            season = "Fall",
            year = 2022,
            genres = listOf("Action", "Supernatural", "Gore"),
            trailerYoutubeId = "jk7Qv9w_XBo",
            studio = "MAPPA"
        ),
        Anime(
            id = 50265,
            title = "Spy x Family Season 2",
            titleJapanese = "SPY×FAMILY",
            synopsis = "Master spy Twilight must build a sham family to investigate a high-ranking politician. Little does he know that his adopted daughter is an Esper who reads minds, and his fabricated wife is a deadly assassin. Together, they navigate secret missions, school exams, and family cruise vacations.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1506/138982l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&q=80",
            score = 8.16,
            scoredBy = 310000,
            rank = 190,
            popularity = 30,
            episodes = 12,
            status = "Finished Airing",
            rating = "PG-13",
            season = "Fall",
            year = 2023,
            genres = listOf("Action", "Comedy", "Slice of Life"),
            trailerYoutubeId = "OqdYnAxke2E",
            studio = "Wit Studio & CloverWorks"
        ),
        Anime(
            id = 50709,
            title = "Oshi no Ko",
            titleJapanese = "【推しの子】",
            synopsis = "In the entertainment world, lies are weapons. Gorou Amemiya is a countryside gynecologist who happens to be an ardent fan of the rising 16-year-old idol Ai Hoshino. When Ai appears at his clinic pregnant with twins, Gorou vows to safely deliver her babies. However, on the eve of birth, Gorou is murdered—and reincarnates as Ai's twin son, Aquamarine!",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1812/134736l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=1200&q=80",
            score = 8.68,
            scoredBy = 430000,
            rank = 62,
            popularity = 40,
            episodes = 11,
            status = "Finished Airing",
            rating = "PG-13",
            season = "Spring",
            year = 2023,
            genres = listOf("Drama", "Mystery", "Supernatural"),
            trailerYoutubeId = "ZRtdQ81jPUQ",
            studio = "Doga Kobo"
        ),
        Anime(
            id = 5114,
            title = "Fullmetal Alchemist: Brotherhood",
            titleJapanese = "鋼の錬金術師 FULLMETAL ALCHEMIST",
            synopsis = "After a horrific alchemy experiment goes wrong in the Elric household, brothers Edward and Alphonse are left in catastrophic new realities. Ignoring the alchemical restriction against human transmutation, the boys attempted to bring their recently deceased mother back to life. Edward lost his left leg and Alphonse his entire body. Edward sacrifices his right arm to bind Alphonse's soul to a suit of armor.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1208/94745l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&q=80",
            score = 9.10,
            scoredBy = 2100000,
            rank = 3,
            popularity = 3,
            episodes = 64,
            status = "Finished Airing",
            rating = "R - 17+",
            season = "Spring",
            year = 2009,
            genres = listOf("Action", "Adventure", "Fantasy"),
            trailerYoutubeId = "--IcmZkvL0Q",
            studio = "Bones"
        ),
        Anime(
            id = 21,
            title = "One Piece: Egghead Arc",
            titleJapanese = "ONE PIECE",
            synopsis = "Barely surviving the harsh waters of the Grand Line, Monkey D. Luffy and the Straw Hat Pirates arrive at the futuristic island of Egghead, where the genius scientist Dr. Vegapunk resides. Unveiling secrets of the Ancient Kingdom and the Void Century, Luffy faces Admiral Kizaru and the world elders in epic Gear 5 clashes.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1244/138851l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1200&q=80",
            score = 8.73,
            scoredBy = 1250000,
            rank = 42,
            popularity = 2,
            episodes = 1110,
            status = "Currently Airing",
            rating = "PG-13",
            season = "Winter",
            year = 2024,
            genres = listOf("Action", "Adventure", "Fantasy"),
            trailerYoutubeId = "A9_mBv7K6v4",
            studio = "Toei Animation"
        ),
        Anime(
            id = 48583,
            title = "Attack on Titan: The Final Season",
            titleJapanese = "進撃の巨人 The Final Season",
            synopsis = "Turning against his former comrades and the entire world, Eren Yeager initiates the Rumbling. With countless Colossal Titans marching across the continents, Eren intends to exterminate all life outside of Paradis Island. Mikasa, Armin, Levi, and their allies must band together in a desperate final battle.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1279/131078l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=1200&q=80",
            score = 9.04,
            scoredBy = 1100000,
            rank = 5,
            popularity = 1,
            episodes = 28,
            status = "Finished Airing",
            rating = "R - 17+",
            season = "Winter",
            year = 2022,
            genres = listOf("Action", "Drama", "Suspense"),
            trailerYoutubeId = "M_OauHnAFc8",
            studio = "MAPPA"
        ),
        Anime(
            id = 52588,
            title = "Kaiju No. 8",
            titleJapanese = "怪獣8号",
            synopsis = "Monsters known as 'Kaiju' have been appearing across Japan for years. Kafka Hibino, who cleans up after Kaiju battles, has always dreamed of joining the Japan Defense Force. After an encounter with a small parasitic creature that grants him the ability to transform into a humanoid Kaiju, Kafka earns code name Kaiju No. 8!",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1170/141380l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=1200&q=80",
            score = 8.24,
            scoredBy = 195000,
            rank = 145,
            popularity = 60,
            episodes = 12,
            status = "Finished Airing",
            rating = "PG-13",
            season = "Spring",
            year = 2024,
            genres = listOf("Action", "Sci-Fi"),
            trailerYoutubeId = "c3ljZxy8v4Y",
            studio = "Production I.G"
        ),
        Anime(
            id = 51009,
            title = "Jujutsu Kaisen Season 1",
            titleJapanese = "呪術廻戦",
            synopsis = "Idly indulging in supernatural activities with the Occult Club, high schooler Yuji Itadori spends his days visiting his bedridden grandfather. However, this leisurely lifestyle takes a turn for the bizarre when he unknowingly swallows a cursed talisman—the decaying finger of the demon Sukuna.",
            imageUrl = "https://cdn.myanimelist.net/images/anime/1171/109222l.jpg",
            bannerUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=1200&q=80",
            score = 8.61,
            scoredBy = 1500000,
            rank = 65,
            popularity = 8,
            episodes = 24,
            status = "Finished Airing",
            rating = "R - 17+",
            season = "Fall",
            year = 2020,
            genres = listOf("Action", "Fantasy", "Supernatural"),
            trailerYoutubeId = "4A_X-Dvl0ws",
            studio = "MAPPA"
        )
    )

    fun getEpisodesForAnime(anime: Anime): List<Episode> {
        val total = (anime.episodes ?: 12).coerceAtMost(24)
        return (1..total).map { num ->
            Episode(
                episodeNumber = num,
                title = when (num) {
                    1 -> "The Awakening"
                    2 -> "If I Had One More Chance"
                    3 -> "It's Like a Quest"
                    4 -> "I've Gotta Get Stronger"
                    5 -> "A Fair Deal"
                    6 -> "The Real Hunt Begins"
                    7 -> "Let's See What You've Got"
                    8 -> "Frustration"
                    9 -> "You've Been Hiding Your Skills"
                    10 -> "What Is This, a Dungeon?"
                    11 -> "A Knight Who Defends an Empty Throne"
                    12 -> "Arise"
                    else -> "Episode $num: Path of Destiny"
                },
                duration = "24m",
                synopsis = "Thrilling episode $num of ${anime.title}. High stakes battle and pivotal character revelations.",
                thumbnailUrl = anime.imageUrl,
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                isWatched = num <= 2,
                progressPercent = if (num == 2) 0.65f else if (num == 1) 1.0f else 0.0f
            )
        }
    }

    fun getCharactersForAnime(animeId: Int): List<AnimeCharacter> {
        return listOf(
            AnimeCharacter("Sung Jinwoo", "Main Protagonist", "https://cdn.myanimelist.net/images/characters/14/501705.jpg"),
            AnimeCharacter("Cha Hae-In", "S-Rank Hunter", "https://cdn.myanimelist.net/images/characters/9/528392.jpg"),
            AnimeCharacter("Go Gunhee", "Hunter Chairman", "https://cdn.myanimelist.net/images/characters/2/528394.jpg"),
            AnimeCharacter("Choi Jong-In", "The Ultimate Soldier", "https://cdn.myanimelist.net/images/characters/7/528393.jpg"),
            AnimeCharacter("Baek Yoonho", "White Tiger Master", "https://cdn.myanimelist.net/images/characters/16/528391.jpg")
        )
    }
}
