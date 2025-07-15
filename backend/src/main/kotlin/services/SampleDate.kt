package services

fun insertSampleData() {
    val user1 = UserService.insertUser("ash", "ash@poke.com", "pikachu123")
    val user2 = UserService.insertUser("misty", "misty@poke.com", "togepi123")

    ScoreService.insertScore(user1, score = 3, correctAnswers = 3, time = 45)
    ScoreService.insertScore(user1, score = 2, correctAnswers = 2, time = 60)

    ScoreService.insertScore(user2, score = 4, correctAnswers = 4, time = 50)
    ScoreService.insertScore(user2, score = 5, correctAnswers = 5, time = 40)
}