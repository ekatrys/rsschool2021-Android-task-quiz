package com.rsschool.quiz

data class Question(
    val text: String,
    val answers: List<String>
)

val questions: MutableList<Question> = mutableListOf(
    Question(
        text = "What is Android Jetpack?",
        answers = listOf("all of these", "tools", "documentation", "libraries", "don't know")
    ),
    Question(
        text = "Base class for Layout?",
        answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot", "don't know")
    ),
    Question(
        text = "Layout for complex Screens?",
        answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout", "don't know")
    ),
    Question(
        text = "Pushing structured data into a Layout?",
        answers = listOf("Data Binding", "Data Pushing", "Set Text", "OnClick", "don't know")
    ),
    Question(
        text = "Inflate layout in fragments?",
        answers = listOf("onCreateView", "onViewCreated", "onCreateLayout", "onInflateLayout", "don't know")
    )
)