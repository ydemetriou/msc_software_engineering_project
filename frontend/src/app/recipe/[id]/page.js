"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";

export default function RecipeDetails() {
  const { id } = useParams(); // Παίρνουμε το ID από το URL
  const router = useRouter();

  const [recipe, setRecipe] = useState(null);
  const [completedStepIds, setCompletedStepIds] = useState([]); // Ποια βήματα ολοκληρώθηκαν
  const [progress, setProgress] = useState(0); // Ποσοστό %

  // 1. Λήψη της συνταγής από το Backend
  useEffect(() => {
    fetch(`http://localhost:8081/api/recipes/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error("Recipe not found");
        return res.json();
      })
      .then((data) => setRecipe(data))
      .catch((err) => {
        alert("Η συνταγή δεν βρέθηκε!");
        router.push("/");
      });
  }, [id, router]);

  // 2. Υπολογισμός Προόδου (Κάθε φορά που τσεκάρουμε ένα βήμα)
  // 2. Υπολογισμός Προόδου (ΔΙΟΡΘΩΜΕΝΟ)
  useEffect(() => {
    if (!recipe) return;

    // Βρες τα βήματα που έχουν ολοκληρωθεί
    const completedSteps = recipe.steps.filter((step) =>
      completedStepIds.includes(step.id)
    );

    // Άθροισε τον χρόνο των ολοκληρωμένων
    const completedTime = completedSteps.reduce(
      (sum, step) => sum + step.duration,
      0
    );

    // Υπολόγιζουμε τον ΠΡΑΓΜΑΤΙΚΟ συνολικό χρόνο (άθροισμα όλων των βημάτων)
    // Αν τα βήματα δεν έχουν χρόνο, χρησιμοποίησε το γενικό χρόνο της συνταγής ως fallback
    const stepsTotalDuration = recipe.steps.reduce(
      (sum, step) => sum + step.duration,
      0
    );
    const totalCalculationTime =
      stepsTotalDuration > 0 ? stepsTotalDuration : recipe.totalTime;

    // Αποφυγή διαίρεσης με το 0
    const finalDivisor = totalCalculationTime > 0 ? totalCalculationTime : 1;

    let percentage = (completedTime / finalDivisor) * 100;

    if (percentage > 100) percentage = 100;
    setProgress(Math.round(percentage));
  }, [completedStepIds, recipe]);

  // Toggle ολοκλήρωσης βήματος
  const toggleStep = (stepId) => {
    if (completedStepIds.includes(stepId)) {
      setCompletedStepIds(completedStepIds.filter((id) => id !== stepId)); // Ξε-τσεκάρισμα
    } else {
      setCompletedStepIds([...completedStepIds, stepId]); // Τσεκάρισμα
    }
  };

  if (!recipe) return <div className="text-center mt-10">Φόρτωση...</div>;

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-4xl mx-auto bg-white rounded-xl shadow-lg overflow-hidden">
        {/* Header Συνταγής */}
        <div className="bg-blue-600 p-8 text-white relative">
          <button
            onClick={() => router.push("/")}
            className="absolute top-4 left-4 text-white hover:underline"
          >
            ← Πίσω
          </button>
          <h1 className="text-4xl font-bold mb-2">{recipe.name}</h1>
          <div className="flex gap-4 text-blue-100">
            <span>📂 {recipe.category}</span>
            <span>⏱️ {recipe.totalTime} λεπτά</span>
            <span>📊 {recipe.difficulty}</span>
          </div>
        </div>

        <div className="p-8">
          {/* Μπάρα Προόδου (Progress Bar) */}
          <div className="mb-8 sticky top-0 bg-white py-4 z-10 border-b">
            <div className="flex justify-between mb-1">
              <span className="font-bold text-gray-700">Πρόοδος Εκτέλεσης</span>
              <span className="font-bold text-blue-600">{progress}%</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-4">
              <div
                className="bg-green-500 h-4 rounded-full transition-all duration-500 ease-out"
                style={{ width: `${progress}%` }}
              ></div>
            </div>
            <p className="text-xs text-gray-500 mt-1">
              *Η πρόοδος υπολογίζεται βάσει του χρόνου των βημάτων.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Στήλη 1: Υλικά */}
            <div className="md:col-span-1 bg-orange-50 p-6 rounded-lg h-fit">
              <h3 className="text-xl font-bold mb-4 text-orange-800">
                🛒 Υλικά
              </h3>
              <ul className="space-y-2">
                {recipe.ingredients.map((ing) => (
                  <li
                    key={ing.id}
                    className="flex items-center gap-2 border-b pb-1 border-orange-200"
                  >
                    <input
                      type="checkbox"
                      className="w-4 h-4 text-orange-600"
                    />
                    <span>
                      {ing.quantity} {ing.unit} <strong>{ing.name}</strong>
                    </span>
                  </li>
                ))}
              </ul>
            </div>

            {/* Στήλη 2: Βήματα (Execution) */}
            <div className="md:col-span-2">
              <h3 className="text-xl font-bold mb-4 text-gray-800">
                👣 Εκτέλεση
              </h3>
              <div className="space-y-4">
                {recipe.steps
                  .sort((a, b) => a.stepOrder - b.stepOrder)
                  .map((step) => (
                    <div
                      key={step.id}
                      className={`p-4 border-2 rounded-lg transition-all cursor-pointer shadow-sm ${
                        completedStepIds.includes(step.id)
                          ? "border-green-500 bg-green-50 opacity-80"
                          : "border-gray-200 hover:border-blue-300 bg-white"
                      }`}
                      onClick={() => toggleStep(step.id)}
                    >
                      {/* 1. Header: Title & Time */}
                      <div className="flex justify-between items-center mb-3">
                        <h4 className="font-bold text-lg text-gray-800">
                          {step.stepOrder}. {step.title}
                        </h4>
                        <span className="bg-gray-100 text-gray-700 text-xs font-bold px-2 py-1 rounded">
                          ⏱️ {step.duration}'
                        </span>
                      </div>

                      {/* 2. Description */}
                      <p className="text-gray-600 mb-4 leading-relaxed">
                        {step.description}
                      </p>

                      {/* 3. NEW: Step Ingredients (Yellow Box) */}
                      {step.stepIngredients &&
                        step.stepIngredients.length > 0 && (
                          <div className="mb-4 bg-yellow-50 p-3 rounded-md border border-yellow-100">
                            <span className="text-xs font-bold text-yellow-800 uppercase block mb-1">
                              🛒 Υλικα για αυτο το βημα:
                            </span>
                            <div className="flex flex-wrap gap-2">
                              {step.stepIngredients.map((ing, i) => (
                                <span
                                  key={i}
                                  className="text-sm font-medium text-gray-700"
                                >
                                  • {ing}
                                </span>
                              ))}
                            </div>
                          </div>
                        )}

                      {/* 4. NEW: Step Image */}
                      {step.stepPhotos && step.stepPhotos.length > 0 && (
                        <div className="mt-3 mb-2">
                          <img
                            src={step.stepPhotos[0]}
                            alt={step.title}
                            className="w-full h-48 object-cover rounded-lg border shadow-sm"
                          />
                        </div>
                      )}

                      {/* 5. Status Bar */}
                      <div className="mt-2 text-sm font-semibold flex items-center gap-2">
                        {completedStepIds.includes(step.id) ? (
                          <span className="text-green-600 flex items-center">
                            ✅ Ολοκληρώθηκε
                          </span>
                        ) : (
                          <span className="text-gray-400 flex items-center">
                            ⬜ Κάντε κλικ για ολοκλήρωση
                          </span>
                        )}
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
