package com.example.quantifydemo;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.quantifydemo.Interface.IQuestion;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment implements IQuestion{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private TextView txt_question;

    private TestQuestion question;
    private int questionIndex=-1;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        questionIndex=getArguments().getInt("index",-1);
        question=Common.questionList.get(questionIndex);
        final View itemView= inflater.inflate(R.layout.fragment_question, container, false);
        radioGroup=itemView.findViewById(R.id.radio_group);

        txt_question = itemView.findViewById(R.id.txt_question);
        txt_question.setText(question.getQuestion());

        final RadioButton radioButton1=itemView.findViewById(R.id.chk_box_A);
        radioButton1.setText(question.getAnswer());
        RadioButton radioButton2=itemView.findViewById(R.id.chk_box_B);
        radioButton2.setText(question.getOption2());
        RadioButton radioButton3=itemView.findViewById(R.id.chk_box_C);
        radioButton3.setText(question.getOption3());
        RadioButton radioButton4=itemView.findViewById(R.id.chk_box_D);
        radioButton4.setText(question.getOption4());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=itemView.findViewById(checkedId);
                Toast.makeText(getActivity(),radioButton.getText().toString(),Toast.LENGTH_SHORT).show();

                if(radioButton.getText().toString().equals(question.getAnswer())){
                    Common.answerSheetList.set(questionIndex,new CurrentQuestion(questionIndex,Common.ANSWER_TYPE.RIGHT_ANSWER));
                }
                else{
                    Common.answerSheetList.set(questionIndex,new CurrentQuestion(questionIndex,Common.ANSWER_TYPE.WRONG_ANSWER));
                }
                Common.selected_values=radioButton.getText().toString();
            }
        });

        return itemView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public CurrentQuestion getSelectedAnswer() {

        return null;
    }

    @Override
    public void showCurrentAnswer() {

    }

    @Override
    public void disableAnswer() {

    }

    @Override
    public void resetQuestion() {
        //reset radio group here
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
