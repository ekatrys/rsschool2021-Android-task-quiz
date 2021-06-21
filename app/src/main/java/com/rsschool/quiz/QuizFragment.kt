package com.rsschool.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding
        get() = _binding!!
    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private val numQuestions = 4

    private val theme: Int = when ((0..4).random()) {
        0 -> R.style.Theme_Quiz_First
        1 -> R.style.Theme_Quiz_Second
        2 -> R.style.Theme_Quiz_Three
        3 -> R.style.Theme_Quiz_Fourth
        else -> R.style.Theme_Quiz_Fifth
    }

    fun goBack() = activity?.onBackPressed()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = QuizFragmentArgs.fromBundle(requireArguments())
        var questionIndex = args.questionIndex
        var rightAnswer = args.rightAnswer

        requireContext().setTheme(theme)

        _binding = FragmentQuizBinding.inflate(inflater)
        setQuestions(questionIndex)
        binding.quiz = this

        if (questionIndex == 0) {
            binding.toolbar.navigationIcon = null
            binding.previousButton.isEnabled = false
        }

        binding.questionRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (-1 != checkedId) binding.nextButton.isEnabled = true
        }

        binding.nextButton.setOnClickListener { view ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            val pickAnswer = getAnswerIndex(checkedId)
            if (checkCorrectAnswer(pickAnswer)) rightAnswer++
            navigation(questionIndex, rightAnswer)
        }

        return binding.root
    }

    private fun setQuestions(questionIndex: Int) {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()

        binding.toolbar.title =
            getString(
                R.string.title_android_quiz_question,
                questionIndex + 1,
                numQuestions + 1
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAnswerIndex(checkedId: Int) = when (checkedId) {
        R.id.option_one -> 0
        R.id.option_two -> 1
        R.id.option_three -> 2
        R.id.option_four -> 3
        else -> 4
    }

    private fun navigation(currentQuestionIndex: Int, rightQuestions: Int) {
        var questionIndex = currentQuestionIndex
        if (questionIndex < numQuestions) {
            currentQuestion = questions[questionIndex]
            questionIndex++
            view?.findNavController()?.navigate(
                QuizFragmentDirections.actionToQuizFragment(
                    questionIndex, rightQuestions
                )
            )
        } else {
            view?.findNavController()?.navigate(
                QuizFragmentDirections.actionToResultFragment(rightQuestions)
            )
        }
    }

    private fun checkCorrectAnswer(answerIndex: Int) =
        (answers[answerIndex] == currentQuestion.answers[0])

}

