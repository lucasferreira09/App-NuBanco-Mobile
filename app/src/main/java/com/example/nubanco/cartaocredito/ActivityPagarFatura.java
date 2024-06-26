package com.example.nubanco.cartaocredito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nubanco.contabancaria.ActivityCadastroUser;
import com.example.nubanco.databinding.ActivityFaturaCartaoBinding;
import com.example.nubanco.databinding.ActivityPagarFaturaBinding;
import com.example.nubanco.transferencia.ActivityTransferenciaRealizada;
import com.example.nubanco.MainActivity;
import com.example.nubanco.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ActivityPagarFatura extends AppCompatActivity {

    private ActivityPagarFaturaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPagarFaturaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Double valorFatura = getIntent().getDoubleExtra("valorFatura", 0);
        Double meuSaldoDouble = getIntent().getDoubleExtra("meuSaldoDisponivel", 0);

        binding.faturaAPagar.setText("R$ " + formataValor(valorFatura));
        binding.fatura.setText("R$ " + formataValor(valorFatura));

        if (valorFatura > meuSaldoDouble) {
            binding.descricaoPagarFatura.setText(
                    "O saldo de R$ " + formataValor(meuSaldoDouble) + " não é suficiente para\neste pagamento.");
            binding.viewPagarFatura.setEnabled(false);
        } else {
            binding.descricaoPagarFatura.setText(
                    "O saldo de R$ " + formataValor(meuSaldoDouble) + " é suficiente para\neste pagamento.");
        }

        binding.viewPagarFatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCadastroUser.myBank.pagarFatura(valorFatura);

                Intent actvComprovante = new Intent(ActivityPagarFatura.this, ActivityTransferenciaRealizada.class);
                actvComprovante.putExtra("novoTextoComprovante", "Pagamento Realizado");
                actvComprovante.putExtra("valorComprovante", formataValor(valorFatura));
                startActivity(actvComprovante);

            }

        });
        binding.closePagarFatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public String formataValor(Double valor) {
        Locale locale = new Locale("pt", "BR");
        String padrao = "###,##0.00";
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(padrao);

        return decimalFormat.format(valor);
    }

    public void voltaActivityInicial() {
        finish();
        // Criar um novo intent para a atividade principal
        Intent intent = new Intent(ActivityPagarFatura.this, MainActivity.class);

        // Definir flags para limpar a pilha de atividades e iniciar uma nova tarefa
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Iniciar a atividade principal
        startActivity(intent);
    }
}