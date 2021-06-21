package com.rsschool.quiz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.rsschool.quiz.databinding.FragmentResultBinding
import kotlin.system.exitProcess

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater)
        val application = requireNotNull(activity).application as QuizApplication
        val arg = ResultFragmentArgs.fromBundle(requireArguments())
        val numCorrect = arg.numCorrect
        val numQuestions = 5

        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText(createMessage(numCorrect, application))
            .setType("text/plain")
            .intent.apply {
                putExtra(Intent.EXTRA_SUBJECT, "Quiz result")
            }

        binding.resutText.text =
            getString(R.string.result_text, numCorrect, numQuestions)

        binding.repeatButton.setOnClickListener {
            view?.findNavController()?.navigate(
                QuizFragmentDirections.actionToQuizFragment(
                )
            )
        }
        binding.closeButton.setOnClickListener {
            activity?.finish()
            exitProcess(0)
        }
        binding.shareButton.setOnClickListener {
            startActivity(shareIntent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createMessage(numCorrect: Int, application: QuizApplication): String {
        var count = 1
        var starter = 0
        val mAnswersList = application.pickAnswer
        val stringBuilder = StringBuilder("")
        return stringBuilder.apply {
            append("Your result: $numCorrect из ${questions.size} \n\n")
            for (question in questions) {
                append(
                    "${count++}) ${questions[starter].text}\n" +
                        "Your answer: ${question.answers[mAnswersList[starter]]} \n\n"
                )
            }
        }.toString()
    }
}