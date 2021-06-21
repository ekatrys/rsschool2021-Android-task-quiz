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
        val application = requireNotNull(activity).application as QuizApplication
        var questionIndex = args.questionIndex
        var rightAnswer = args.rightAnswer
        var existPreviousAnswer = args.existPreviousAnswer


        requireContext().setTheme(theme)

        _binding = FragmentQuizBinding.inflate(inflater)
        setQuestions(questionIndex)
        binding.quiz = this

        if(existPreviousAnswer){
            val buttonIndex = application.pickAnswer[questionIndex]
            savedInstanceState?.putInt("buttonIndex", buttonIndex)
            getRadioButtonId(buttonIndex).isChecked = true
            binding.nextButton.isEnabled = true
        }

        if (savedInstanceState != null){
            val buttonIndex = savedInstanceState.getInt("buttonIndex")
            getRadioButtonId(buttonIndex).isChecked = true
            binding.nextButton.isEnabled = true
        }

        if (questionIndex == 0) {
            binding.toolbar.navigationIcon = null
            binding.previousButton.isEnabled = false

        }

        binding.questionRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val buttonIndex = getAnswerIndex(checkedId)
            savedInstanceState?.putInt("buttonIndex", buttonIndex)
            if (-1 != checkedId) binding.nextButton.isEnabled = true
        }

        binding.nextButton.setOnClickListener { view ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            val pickAnswer = getAnswerIndex(checkedId)
            existPreviousAnswer = application.pickAnswer.size > questionIndex + 1

            if(application.pickAnswer.elementAtOrNull(questionIndex) == null) {
                application.pickAnswer.add(questionIndex, pickAnswer)
            } else {
                application.pickAnswer[questionIndex] = pickAnswer
            }
            if (checkCorrectAnswer(pickAnswer)) rightAnswer++

            navigation(questionIndex, rightAnswer, existPreviousAnswer)
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

    private fun navigation(
        currentQuestionIndex: Int,
        rightQuestions: Int,
        existPreviousAnswer: Boolean
    ) {
        var questionIndex = currentQuestionIndex
        if (questionIndex < numQuestions) {
            currentQuestion = questions[questionIndex]
            questionIndex++
            view?.findNavController()?.navigate(
                QuizFragmentDirections.actionToQuizFragment(
                    questionIndex, rightQuestions, existPreviousAnswer
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

    private fun getRadioButtonId(index: Int) = when (index) {
        0 -> binding.optionOne
        1 -> binding.optionTwo
        2 -> binding.optionThree
        3 -> binding.optionFour
        else -> binding.optionFive
    }
}

